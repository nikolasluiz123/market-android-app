package br.com.market.storage.repository

import android.content.Context
import br.com.market.domain.ProductDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.models.Product
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.webclients.ProductWebClient
import br.com.market.storage.extensions.toProductDomain
import br.com.market.storage.extensions.toProductDomainList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject

/**
 * Classe responsável por permitir o acesso as bases de dados.
 *
 * @author Nikolas Luiz Schmitt
 *
 * @property context Contexto da aplicação de uso diverso.
 * @property productDAO Classe responsável por acessar dados locais do Produto.
 * @property productWebClient Classe responsável por acessar dados remotos do Produto.
 * @property brandDAO Classe responsável por acessar dados locais da Marca
 *
 */
class ProductRepository @Inject constructor(
    private val context: Context,
    private val productDAO: ProductDAO,
    private val productWebClient: ProductWebClient,
    private val brandDAO: BrandDAO,
) {

    /**
     * Função responsável por salvar o Produto localmente e, se houver conexão,
     * vai salvar remotamente também.
     *
     * @param productDomain Produto que deseja-se salvar.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun save(productDomain: ProductDomain): PersistenceResponse {
        lateinit var product: Product

        var response = if(productDomain.id == null) {
            product = Product(name = productDomain.name, imageUrl = productDomain.imageUrl)
            productWebClient.saveProduct(product)
        } else {
            product = productDAO.findProductById(productDomain.id!!).first()!!.copy(name = productDomain.name, imageUrl = productDomain.imageUrl)
            productWebClient.updateProduct(product)
        }

        product.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE

        productDAO.saveProduct(product)

        // Fazendo isso para que quando não conseguir se conectar com o servidor retorne sucesso
        // por conta da persistência local
        response.success = response.success || response.code == HttpURLConnection.HTTP_UNAVAILABLE

        response = response.copy(idLocal = product.id, idRemote = response.idRemote)

        return response
    }

    /**
     * Função para realizar as operações de remoção do produto, dependendo do cenário
     * ele será removido fisicamente na base local e remota, mas, há tratativas para caso
     * o serviço retorne algum erro, o registro ficará inativo na base local e pendente de
     * sincronismo.
     *
     * @param productLocalId Id do produto na base local.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun deleteProduct(productLocalId: UUID): MarketServiceResponse {
        productDAO.inactivateProductAndReferences(productLocalId)

        val response = productWebClient.deleteProduct(productLocalId)

        if (response.success) {
            productDAO.deleteProductAndReferences(productLocalId)
        }

        // Fazendo isso para que quando não conseguir se conectar com o servidor retorne sucesso
        // por conta da inativação local
        response.success = response.success || response.code == HttpURLConnection.HTTP_UNAVAILABLE

        return response
    }

    /**
     * Função responsável por realizar o sincronismo dos produtos.
     *
     * Os produtos criados e alterados localmente serão enviados para a base remota
     * e, caso seja bem sucedido o envio, os dados são marcados como sincronizados
     * nos registros locais.
     *
     * Os produtos que foram inativados localmente serão enviados para o servido e lá
     * serão excluidos, se a execução for bem sucedida, os dados serão excluídos localmente
     * também.
     *
     * É feito um tratamento para que, caso haja um erro na primeira etapa (salvar ou alterar os registros),
     * não seja sobrescrita a resposta que contém o erro.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun syncProducts(): MarketServiceResponse {
        val activeProductsToSync = productDAO.findAllActiveProductsNotSynchronized()
        val syncResponse = productWebClient.syncProducts(activeProductsToSync)

        if (syncResponse.success) {
            activeProductsToSync.forEach {
                val product = it.copy(synchronized = true)
                productDAO.saveProduct(product)
            }

            val inactiveAndNotSynchronizedProducts = productDAO.findAllInactiveAndNotSynchronizedProducts()
            val deleteResponse = productWebClient.deleteProducts(inactiveAndNotSynchronizedProducts)

            return if (deleteResponse.success) {
                inactiveAndNotSynchronizedProducts.forEach {
                    productDAO.deleteProductAndReferences(it.id)
                }

                val findProductsResponse = productWebClient.findAllProducts()
                findProductsResponse.values.forEach { productDAO.saveProduct(it) }

                findProductsResponse.toBaseResponse()
            } else {
                deleteResponse
            }
        } else {
            return syncResponse
        }
    }

    /**
     * Função que retorna todos os produtos que devem ser exibidos.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findActiveAllProducts(): Flow<List<ProductDomain>> {
        return productDAO.findActiveAllProducts().toProductDomainList()
    }

    /**
     * Função que retorna um produto específico.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findProductById(productId: UUID): Flow<ProductDomain?> {
        return productDAO.findProductById(productId).toProductDomain()
    }
}