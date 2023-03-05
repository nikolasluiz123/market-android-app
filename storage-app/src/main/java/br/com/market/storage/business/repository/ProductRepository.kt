package br.com.market.storage.business.repository

import android.content.Context
import br.com.market.storage.R
import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.services.response.PersistenceResponse
import br.com.market.storage.business.services.util.TokenUtils
import br.com.market.storage.business.webclient.ProductWebClient
import br.com.market.storage.extensions.toProductDomain
import br.com.market.storage.extensions.toProductDomainList
import br.com.market.storage.preferences.PreferencesKey.TOKEN
import br.com.market.storage.preferences.dataStore
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.utils.TransformClassHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import okhttp3.internal.wait
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val context: Context,
    private val productDAO: ProductDAO,
    private val productWebClient: ProductWebClient,
    private val brandDAO: BrandDAO,
) {

    suspend fun save(productDomain: ProductDomain): PersistenceResponse {
        val token = context.dataStore.data.first()[TOKEN]

        val product = Product()
        TransformClassHelper.domainToModel(domain = productDomain, model = product)

        var response = productWebClient.saveProduct(TokenUtils.formatToken(token!!), product)

        product.synchronized = response.success

        val idLocalProduct = productDAO.saveProduct(product)
        response = response.copy(idLocal = idLocalProduct)

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
}