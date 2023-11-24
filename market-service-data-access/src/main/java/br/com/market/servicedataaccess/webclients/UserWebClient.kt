package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.domain.UserDomain
import br.com.market.models.User
import br.com.market.sdo.AuthenticationRequestSDO
import br.com.market.servicedataaccess.responses.extensions.getAuthenticationResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.IUserService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Classe para acesso aos end points de autenticação do serviço.
 *
 * @property context Contexto de uso diversificado.
 * @property service Interface para acesso ao serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class UserWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IUserService
) : BaseWebClient(context) {

    /**
     * Função para autenticar um usuário.
     *
     * @param userDomain Objeto preenchido com as informações da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        return authenticationServiceErrorHandlingBlock(
            codeBlock = {
                val authenticationRequestSDO = AuthenticationRequestSDO(
                    email = userDomain.email,
                    password = userDomain.password,
                    tempDeviceId = userDomain.tempDeviceId
                )

                service.authenticate(authenticationRequestSDO).getAuthenticationResponseBody()
            }
        )
    }

    suspend fun findAllUsers(marketId: Long): ReadResponse<User> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAllUserSDOs(marketId).getReadResponseBody()

                val users = response.values.map {
                    User(
                        id = it.localId,
                        name = it.name,
                        email = it.email,
                        password = it.password,
                        token = it.token,
                        marketId = it.marketId
                    )
                }

                ReadResponse(values = users, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}