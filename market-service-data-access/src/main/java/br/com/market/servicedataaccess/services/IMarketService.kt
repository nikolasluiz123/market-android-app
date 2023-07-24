package br.com.market.servicedataaccess.services

import br.com.market.sdo.MarketSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface IMarketService {

    @GET("market/{deviceId}")
    suspend fun findByDeviceId(@Path("deviceId") deviceId: String): Response<ReadResponse<MarketSDO>>

}