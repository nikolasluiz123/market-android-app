package br.com.market.servicedataaccess.services

import br.com.market.sdo.brand.BrandBodySDO
import br.com.market.sdo.brand.BrandSDO
import br.com.market.sdo.brand.CategoryBrandSDO
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.responses.ReadResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface IBrandService {

    @POST("brand")
    suspend fun save(@Header("Authorization") token: String, @Body brandBodySDO: BrandBodySDO): Response<PersistenceResponse>

    @POST("brand/toggleActive")
    suspend fun toggleActive(@Header("Authorization") token: String, @Body brandSDO: BrandSDO): Response<PersistenceResponse>

    @POST("brand/sync")
    suspend fun sync(@Header("Authorization") token: String, @Body brandBodySDOs: List<BrandBodySDO>): Response<MarketServiceResponse>

    @GET("brand")
    suspend fun findAllBrandDTOs(@Header("Authorization") token: String): Response<ReadResponse<BrandSDO>>

    @GET("brand/categoryBrand")
    suspend fun findAllCategoryBrandDTOs(@Header("Authorization") token: String): Response<ReadResponse<CategoryBrandSDO>>

}