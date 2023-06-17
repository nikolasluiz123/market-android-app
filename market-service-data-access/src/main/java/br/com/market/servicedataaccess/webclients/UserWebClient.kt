package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.domain.UserDomain
import br.com.market.models.User
import br.com.market.sdo.user.AuthenticationRequestSDO
import br.com.market.sdo.user.UserSDO
import br.com.market.servicedataaccess.responses.extensions.getAuthenticationResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
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
     * Função para registrar um usuário.
     *
     * @param user Objeto preenchido com as informações da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun registerUser(user: User): AuthenticationResponse {
        return authenticationServiceErrorHandlingBlock(
            codeBlock = {
                val userSDO = UserSDO(
                    localId = user.id,
                    name = user.name!!,
                    email = user.email!!,
                    password = user.password!!
                )

                service.register(userSDO).getAuthenticationResponseBody()
            }
        )
    }

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
                    password = userDomain.password
                )

                service.authenticate(authenticationRequestSDO).getAuthenticationResponseBody()
            }
        )
    }

    suspend fun sync(users: List<User>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val userSDOs = users.map {
                    UserSDO(
                        localId = it.id,
                        active = it.active,
                        name = it.name!!,
                        email = it.email!!,
                        password = it.password!!
                    )
                }

                service.sync(getToken(), userSDOs).getResponseBody()
            }
        )
    }

    suspend fun findAllUsers(): ReadResponse<User> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAllUserSDOs(getToken()).getReadResponseBody()

                val users = response.values.map {
                    User(
                        id = it.localId,
                        name = it.name,
                        synchronized = true,
                        active = it.active,
                        email = it.email,
                        password = it.password,
                        token = it.token
                    )
                }

                ReadResponse(values = users, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}