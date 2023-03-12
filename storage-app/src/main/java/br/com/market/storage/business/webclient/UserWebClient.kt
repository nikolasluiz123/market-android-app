package br.com.market.storage.business.webclient

import android.content.Context
import br.com.market.storage.R
import br.com.market.storage.business.sdo.user.AuthenticationRequestSDO
import br.com.market.storage.business.sdo.user.RegisterRequestSDO
import br.com.market.storage.business.services.UserService
import br.com.market.storage.business.services.response.AuthenticationResponse
import br.com.market.storage.business.webclient.extensions.getAuthenticationResponseBody
import br.com.market.storage.ui.domains.UserDomain
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Classe para acesso aos end points de autenticação do serviço.
 *
 * @property context Contexto de uso diversificado.
 * @property userService Interface para acesso ao serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class UserWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userService: UserService
) : BaseWebClient(context) {

    /**
     * Função para registrar um usuário.
     *
     * @param userDomain Objeto preenchido com as informações da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun registerUser(userDomain: UserDomain): AuthenticationResponse {
        return authenticationServiceErrorHandlingBlock(
            codeBlock = {
                val registerRequestSDO = RegisterRequestSDO(
                    name = userDomain.name,
                    email = userDomain.email,
                    password = userDomain.password
                )

                userService.register(registerRequestSDO).getAuthenticationResponseBody()
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

                userService.authenticate(authenticationRequestSDO).getAuthenticationResponseBody()
            }
        )
    }
}