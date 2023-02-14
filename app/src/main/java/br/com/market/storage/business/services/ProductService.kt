package br.com.market.storage.business.services

import br.com.market.storage.business.sdo.product.DeleteProductSDO
import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.sdo.product.ProductViewSDO
import br.com.market.storage.business.sdo.product.UpdateProductSDO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProductService {

    @GET("product")
    suspend fun findAllProducts(): List<ProductViewSDO>

    @POST("product")
    suspend fun saveProduct(productDTO: NewProductSDO): Response<Void>

    @PUT("product")
    suspend fun updateProduct(productDTO: UpdateProductSDO): Response<Void>

    @DELETE("product")
    suspend fun deleteProduct(productDTO: DeleteProductSDO): Response<Void>
}