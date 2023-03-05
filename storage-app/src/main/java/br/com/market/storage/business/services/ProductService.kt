package br.com.market.storage.business.services

import br.com.market.storage.business.sdo.product.DeleteProductSDO
import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.sdo.product.ProductViewSDO
import br.com.market.storage.business.sdo.product.UpdateProductSDO
import br.com.market.storage.business.services.response.PersistenceResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @GET("product")
    suspend fun findAllProducts(@Header("Authorization") token: String): List<ProductViewSDO>

    @POST("product")
    suspend fun saveProduct(@Header("Authorization") token: String, @Body productDTO: NewProductSDO): Response<PersistenceResponse>

    @PUT("product")
    suspend fun updateProduct(@Header("Authorization") token: String, productDTO: UpdateProductSDO): Response<Void>

    @DELETE("product")
    suspend fun deleteProduct(@Header("Authorization") token: String, productDTO: DeleteProductSDO): Response<Void>
}