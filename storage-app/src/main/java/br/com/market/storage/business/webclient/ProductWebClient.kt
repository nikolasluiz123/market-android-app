package br.com.market.storage.business.webclient

import android.content.Context
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.sdo.product.DeleteProductSDO
import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.sdo.product.UpdateProductSDO
import br.com.market.storage.business.services.ProductService
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import br.com.market.storage.business.webclient.extensions.getPersistenceResponseBody
import br.com.market.storage.business.webclient.extensions.getResponseBody
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


/**
 * Classe usada para realizar operações dos end points do produto.
 *
 * @author Nikolas Luiz Schmitt
 *
 * @property context Contexto de uso diversificado
 * @property productService Interface para acesso do serviço.
 *
 */
class ProductWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productService: ProductService
) : BaseWebClient(context) {

    /**
     * Função para salvar um produto na base de dados remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param product Produto que deseja salvar na base remota.
     *
     */
    suspend fun saveProduct(product: Product): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val newProductSDO = NewProductSDO(localProductId = product.id, name = product.name, imageUrl = product.imageUrl)
                productService.saveProduct(getToken(), newProductSDO).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para alterar um produto na base de dados remota.
     *
     * @param product Produto que deseja alterar na base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun updateProduct(product: Product): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val updateProductSDO = UpdateProductSDO(localProductId = product.id, name = product.name, imageUrl = product.imageUrl)
                productService.updateProduct(getToken(), updateProductSDO).getPersistenceResponseBody()
            }
        )
    }

    /**
     * Função para deletar o Produto e todos os registros que possuem referência.
     *
     * @param productLocalId Id do produto gerado na base local.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun deleteProduct(productLocalId: Long): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                productService.deleteProduct(getToken(), DeleteProductSDO(productLocalId)).getResponseBody()
            }
        )
    }

    /**
     * Função responsável par deletar 'N' produtos da base remota, utilizada no sincronismo
     * das bases.
     *
     * @param products Produtos que serão deletados na base remota
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun deleteProducts(products: List<Product>): MarketServiceResponse {
        val productSDOs = products.map { DeleteProductSDO(it.id!!) }

        return serviceErrorHandlingBlock(
            codeBlock = {
                productService.deleteProducts(getToken(), productSDOs).getResponseBody()
            }
        )
    }

    /**
     * Função que sincroniza as informações dos produtos, enviando quem foi criado e quem foi alterado
     * para a base remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param products Produtos que deseja sincronizar.
     *
     */
    suspend fun syncProducts(products: List<Product>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val dtoList = products.map { NewProductSDO(localProductId = it.id, name = it.name, imageUrl = it.imageUrl) }
                productService.syncProducts(getToken(), dtoList).getResponseBody()
            }
        )
    }
}