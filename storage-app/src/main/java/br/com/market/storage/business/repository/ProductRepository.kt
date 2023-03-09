package br.com.market.storage.business.repository

import android.content.Context
import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import br.com.market.storage.utils.TokenUtils
import br.com.market.storage.business.webclient.ProductWebClient
import br.com.market.storage.extensions.toProductDomain
import br.com.market.storage.extensions.toProductDomainList
import br.com.market.storage.preferences.PreferencesKey.TOKEN
import br.com.market.storage.preferences.dataStore
import br.com.market.storage.preferences.getToken
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.utils.TransformClassHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.net.HttpURLConnection
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
        val product = Product()
        TransformClassHelper.domainToModel(domain = productDomain, model = product)

        val idLocalProduct = productDAO.saveProduct(product)

        var response = if(product.id == null) {
            product.id = idLocalProduct
            productWebClient.saveProduct(product)
        } else {
            productWebClient.updateProduct(product)
        }

        product.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE

        response = response.copy(idLocal = idLocalProduct, idRemote = response.idRemote)

        return response
    }

    fun findAllProducts(): Flow<List<ProductDomain>> {
        return productDAO.findAllProducts().toProductDomainList()
    }

    fun findProductById(productId: Long?): Flow<ProductDomain?> {
        return productDAO.findProductById(productId).toProductDomain()
    }

    suspend fun deleteProduct(id: Long) {
        productDAO.deleteProductAndReferences(id)
    }

    suspend fun deleteBrandAndReferences(productId: Long) {
        brandDAO.deleteBrandAndReferences(productId)
    }

    /**
     * Função responsável por contar os registros que foram criados ou
     * alterados apenas localmente.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findCountOfNotSynchronizedRegisters(): Flow<Long> {
        return productDAO.findCountOfNotSynchronizedRegisters()
    }

    /**
     * Função responsável por realizar o sincronismo dos produtos.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun syncProducts(): MarketServiceResponse {
        val productsToSync = productDAO.findAllProductsNotSynchronized()
        val response = productWebClient.syncProducts(productsToSync)

        if (response.success) {
            productsToSync.forEach {
                val product = it.copy(synchronized = true)
                productDAO.saveProduct(product)
            }
        }

        return response
    }
}