package br.com.market.servicedataaccess.services

import br.com.market.sdo.ViaCepSDO
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IViaCepService {

    @GET("viacep/cep")
    suspend fun getAddressByCep(@Query("cep") cep: String): Response<SingleValueResponse<ViaCepSDO>>
}