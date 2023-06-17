package br.com.market.servicedataaccess.services

import br.com.market.sdo.user.AuthenticationRequestSDO
import br.com.market.sdo.user.UserSDO
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interface utilizada para acessar os end points de autenticação do serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
interface IUserService {

    /**
     * Função utilizada para cadastrar um usuário.
     *
     * @param userSDO Objeto com as informações para cadastrar o usuário.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("user/register")
    suspend fun register(@Body userSDO: UserSDO): Response<AuthenticationResponse>

    /**
     * Função responsável por autenticar um usuário.
     *
     * @param authenticationRequestSDO Objeto com as informações para autenticar o usuário.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("user/authenticate")
    suspend fun authenticate(@Body authenticationRequestSDO: AuthenticationRequestSDO): Response<AuthenticationResponse>

    @POST("user/sync")
    suspend fun sync(@Header("Authorization") token: String, @Body userSDOs: List<UserSDO>): Response<MarketServiceResponse>

    @GET("user")
    suspend fun findAllUserSDOs(@Header("Authorization") token: String): Response<ReadResponse<UserSDO>>
}