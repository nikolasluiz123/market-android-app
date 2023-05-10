package br.com.market.servicedataaccess.services

import br.com.market.sdo.product.ProductBodySDO
import br.com.market.servicedataaccess.responses.PersistenceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface IProductService {

    @POST("product")
    suspend fun save(@Header("Authorization") token: String, @Body productBodySDO: ProductBodySDO): Response<PersistenceResponse>

}