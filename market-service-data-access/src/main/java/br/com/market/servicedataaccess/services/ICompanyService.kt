package br.com.market.servicedataaccess.services

import br.com.market.sdo.CompanySDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface ICompanyService {

    @GET("company/{deviceId}")
    suspend fun findByDeviceId(@Header("Authorization") token: String, @Path("deviceId") deviceId: String): Response<ReadResponse<CompanySDO>>

}