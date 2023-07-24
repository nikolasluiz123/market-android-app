package br.com.market.servicedataaccess.services

import br.com.market.sdo.DeviceSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface IDeviceService {

    @GET("device/{id}")
    suspend fun findById(@Path("id") id: String): Response<ReadResponse<DeviceSDO>>


}