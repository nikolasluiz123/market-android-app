package br.com.market.servicedataaccess.services

import br.com.market.sdo.ClientSDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface IClientService {

    @POST("client")
    suspend fun save(@Body clientSDO: ClientSDO): Response<PersistenceResponse>

    @GET("client/email")
    suspend fun isUniqueEmail(@Query("email") email: String): Response<SingleValueResponse<Boolean>>

    @GET("client/cpf")
    suspend fun isUniqueCPF(@Query("cpf") cpf: String): Response<SingleValueResponse<Boolean>>
}