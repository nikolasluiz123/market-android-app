package br.com.market.servicedataaccess.services

import br.com.market.sdo.CategorySDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import retrofit2.Response
import retrofit2.http.*


interface ICategoryService {

    @POST("category")
    suspend fun save(@Header("Authorization") token: String, @Body categorySDO: CategorySDO): Response<PersistenceResponse>

    @POST("category/toggleActive/{id}")
    suspend fun toggleActive(@Header("Authorization") token: String, @Path("id") id: String): Response<PersistenceResponse>

    @GET("category")
    suspend fun getListCategory(
        @Header("Authorization") token: String,
        @Query("simpleFilter") simpleFilter: String?,
        @Query("marketId") marketId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ReadResponse<CategorySDO>>

    @GET("category/{id}")
    suspend fun findCategoryByLocalId(@Header("Authorization") token: String, @Path("id") id: String): Response<SingleValueResponse<CategorySDO>>
}