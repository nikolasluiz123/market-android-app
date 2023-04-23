package br.com.market.servicedataaccess.services

import br.com.market.sdo.CategorySDO
import br.com.market.sdo.brand.*
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.responses.ReadResponse
import retrofit2.Response
import retrofit2.http.*


interface CategoryService {

    @POST("category")
    suspend fun save(@Header("Authorization") token: String, @Body categorySDO: CategorySDO): Response<PersistenceResponse>

    @POST("category/toggleActive")
    suspend fun toggleActive(@Header("Authorization") token: String, @Body categorySDO: CategorySDO): Response<PersistenceResponse>

    @POST("category/sync")
    suspend fun sync(@Header("Authorization") token: String, @Body categoriesSDOs: List<CategorySDO>): Response<MarketServiceResponse>

    @GET("category")
    suspend fun findAll(@Header("Authorization") token: String): Response<ReadResponse<CategorySDO>>

}