package br.com.market.servicedataaccess.services

import br.com.market.sdo.BrandBodySDO
import br.com.market.sdo.BrandReadSDO
import br.com.market.sdo.CategoryBrandSDO
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface IBrandService {

    @POST("brand")
    suspend fun save(@Header("Authorization") token: String, @Body brandBodySDO: BrandBodySDO): Response<PersistenceResponse>

    @POST("brand/toggleActive")
    suspend fun toggleActive(@Header("Authorization") token: String, @Body categoryBrandSDO: CategoryBrandSDO): Response<PersistenceResponse>

    @POST("brand/sync")
    suspend fun sync(@Header("Authorization") token: String, @Body brandBodySDOs: List<BrandBodySDO>): Response<MarketServiceResponse>

    @GET("brand/lov")
    suspend fun getListLovBrandReadDTO(
        @Header("Authorization") token: String,
        @Query("simpleFilter") simpleFilter: String?,
        @Query("marketId") marketId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ReadResponse<BrandReadSDO>>

//    @GET("brand/categoryBrand")
//    suspend fun findCategoryBrandSDOs(
//        @Header("Authorization") token: String,
//        @Query("marketId") marketId: Long,
//        @Query("limit") limit: Int? = null,
//        @Query("offset") offset: Int? = null
//    ): Response<ReadResponse<CategoryBrandSDO>>

}