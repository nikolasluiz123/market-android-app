package br.com.market.storage.business.webclient

import android.content.Context
import br.com.market.storage.R
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.services.ProductService
import br.com.market.storage.business.services.response.PersistenceResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.http.POST
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class ProductWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productService: ProductService
) {

//    suspend fun findAllProducts(): List<Product> {
//        return productService.findAllProducts().map(ProductMapper::toProductModel)
//    }

    @POST("product")
    suspend fun saveProduct(token: String, product: Product): PersistenceResponse {
        return try {
            val newProductSDO = NewProductSDO(name = product.name, imageUrl = product.imageUrl)
            productService.saveProduct(token, newProductSDO).body()!!
        } catch (e: SocketTimeoutException) {
            PersistenceResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                success = false,
                error = context.getString(R.string.save_product_socket_timeout_error_message)
            )
        }
    }
//
//    suspend fun updateProduct(product: Product, brands: List<Brand>): Response<Void> {
//        val updateProductSDO = ProductMapper.toUpdateProductSDO(product, brands)
//        return productService.updateProduct(updateProductSDO)
//    }
//
//    suspend fun deleteProduct(product: Product): Response<Void> {
//        val deleteProductSDO = ProductMapper.toDeleteProductSDO(product)
//        return productService.deleteProduct(deleteProductSDO)
//    }
}