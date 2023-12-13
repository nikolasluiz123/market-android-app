package br.com.market.servicedataaccess.services

import br.com.market.sdo.BrandAndReferencesSDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface IBrandService {

    @POST("brand")
    suspend fun save(@Header("Authorization") token: String, @Body brandAndReferencesSDO: BrandAndReferencesSDO): Response<PersistenceResponse>

    @POST("brand/toggleActive/{categoryId}/{brandId}")
    suspend fun toggleActive(
        @Header("Authorization") token: String,
        @Path("categoryId") categoryId: String,
        @Path("brandId") brandId: String
    ): Response<PersistenceResponse>

    @GET("brand")
    suspend fun getListBrand(
        @Header("Authorization") token: String,
        @Query("simpleFilter") simpleFilter: String?,
        @Query("categoryLocalId") categoryLocalId: String?,
        @Query("marketId") marketId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ReadResponse<BrandAndReferencesSDO>>

    @GET("brand/{categoryId}/{brandId}")
    suspend fun findBrandByLocalId(
        @Header("Authorization") token: String,
        @Path("categoryId") categoryId: String,
        @Path("brandId") brandId: String
    ): Response<SingleValueResponse<BrandAndReferencesSDO>>
}