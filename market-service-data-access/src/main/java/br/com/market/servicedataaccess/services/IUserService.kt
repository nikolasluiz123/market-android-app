package br.com.market.servicedataaccess.services

import br.com.market.sdo.user.AuthenticationRequestSDO
import br.com.market.sdo.user.RegisterRequestSDO
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import retrofit2.Response
import retrofit2.http.Body
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
     * @param registerRequestSDO Objeto com as informações para cadastrar o usuário.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("auth/register")
    suspend fun register(@Body registerRequestSDO: RegisterRequestSDO): Response<AuthenticationResponse>

    /**
     * Função responsável por autenticar um usuário.
     *
     * @param authenticationRequestSDO Objeto com as informações para autenticar o usuário.
     *
     * @author Nikolas Luiz Schmitt
     */
    @POST("auth/authenticate")
    suspend fun authenticate(@Body authenticationRequestSDO: AuthenticationRequestSDO): Response<AuthenticationResponse>
}