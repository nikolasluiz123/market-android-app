package br.com.market.storage.repository

import br.com.market.domain.UserDomain
import br.com.market.localdataaccess.dao.UserDAO
import br.com.market.models.User
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.webclients.UserWebClient
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * Classe Repository responsável por permitir manipular
 * o usuário, atualmente somente via WebClient.
 *
 * @property context Context do App para uso geral.
 * @property webClient WebClient para acesso aos end points do usuário no serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class UserRepository @Inject constructor(
    private val dao: UserDAO,
    private val webClient: UserWebClient
) {

    /**
     * Função responsável por cadastrar um novo usuário.
     *
     * @param userDomain Objeto com os dados do usuário recuperados da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun registerUser(userDomain: UserDomain): AuthenticationResponse {
        val user = User(
            name = userDomain.name,
            email = userDomain.email,
            password = userDomain.password
        )

        userDomain.id = user.id

        val response = webClient.registerUser(user)

        user.synchronized = response.getObjectSynchronized()
        user.token = response.token

        dao.save(user)

        return response
    }

    /**
     * Função responsável por autenticar um usuário, iniciando sua seção.
     *
     * @param userDomain Objeto com os dados do usuário recuperados da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        val response = webClient.authenticate(userDomain = userDomain)

        if (!response.success) {
            dao.findUserByEmail(userDomain.email)?.let { user ->
                return AuthenticationResponse(
                    token = user.token,
                    userLocalId = user.id,
                    code = HttpURLConnection.HTTP_OK,
                    success = true
                )
            }
        }

        return response
    }

    suspend fun sync(): MarketServiceResponse {
        val response = sendUsersToRemoteDB()
        return if (response.success) updateUsersOfLocalDB() else response
    }

    private suspend fun sendUsersToRemoteDB(): MarketServiceResponse {
        val usersNotSynchronized = dao.findUsersNotSynchronized()
        val response = webClient.sync(usersNotSynchronized)

        if (response.success) {
            val productsSynchronized = usersNotSynchronized.map { it.copy(synchronized = true) }
            dao.save(productsSynchronized)
        }

        return response
    }

    private suspend fun updateUsersOfLocalDB(): MarketServiceResponse {
        val responseFindAllProducts = webClient.findAllUsers()

        if (responseFindAllProducts.success) {
            dao.save(responseFindAllProducts.values)
        }

        return responseFindAllProducts.toBaseResponse()
    }
}