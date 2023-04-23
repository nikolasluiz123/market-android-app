package br.com.market.servicedataaccess.services

import br.com.market.sdo.BrandSDO
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.responses.ReadResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface BrandService {

    @POST("brand")
    suspend fun save(@Header("Authorization") token: String, @Body brandSDO: BrandSDO): Response<PersistenceResponse>

    @POST("brand/toggleActive")
    suspend fun toggleActive(@Header("Authorization") token: String, @Body brandSDO: BrandSDO): Response<PersistenceResponse>

    @POST("brand/sync")
    suspend fun sync(@Header("Authorization") token: String, @Body brandsSDOs: List<BrandSDO>): Response<MarketServiceResponse>

    @GET("brand")
    suspend fun findAll(@Header("Authorization") token: String): Response<ReadResponse<BrandSDO>>

}