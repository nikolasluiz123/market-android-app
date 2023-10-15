package br.com.market.servicedataaccess.services

import br.com.market.sdo.MarketReadSDO
import br.com.market.sdo.MarketSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface IMarketService {

    @GET("market/{deviceId}")
    suspend fun findByDeviceId(@Path("deviceId") deviceId: String): Response<ReadResponse<MarketSDO>>

    @GET("market/lov")
    suspend fun getListLovMarketReadDTO(
        @Header("Authorization") token: String,
        @Query("simpleFilter") simpleFilter: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ReadResponse<MarketReadSDO>>
}