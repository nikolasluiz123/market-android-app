package br.com.market.storage.business.services

import br.com.market.storage.business.sdo.user.AuthenticationRequestSDO
import br.com.market.storage.business.sdo.user.RegisterRequestSDO
import br.com.market.storage.business.services.response.AuthenticationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("auth/register")
    suspend fun register(@Body registerRequestSDO: RegisterRequestSDO): Response<AuthenticationResponse>

    @POST("auth/authenticate")
    suspend fun authenticate(@Body authenticationRequestSDO: AuthenticationRequestSDO): Response<AuthenticationResponse>
}