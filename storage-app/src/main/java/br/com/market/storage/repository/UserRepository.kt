package br.com.market.storage.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.domain.UserDomain
import br.com.market.servicedataaccess.responses.AuthenticationResponse
import br.com.market.servicedataaccess.webclients.UserWebClient
import javax.inject.Inject

/**
 * Classe Repository responsável por permitir manipular
 * o usuário, atualmente somente via WebClient.
 *
 * @property context Context do App para uso geral.
 * @property userWebClient WebClient para acesso aos end points do usuário no serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class UserRepository @Inject constructor(
    private val context: Context,
    private val userWebClient: UserWebClient
) {

    /**
     * Função responsável por cadastrar um novo usuário.
     *
     * @param userDomain Objeto com os dados do usuário recuperados da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun registerUser(userDomain: UserDomain): AuthenticationResponse {
        return userWebClient.registerUser(userDomain)
    }

    /**
     * Função responsável por autenticar um usuário, iniciando sua seção.
     *
     * @param userDomain Objeto com os dados do usuário recuperados da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        val response = userWebClient.authenticate(userDomain = userDomain)

        if (response.success) {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKey.TOKEN] = response.token!!
            }
        }

        return response
    }

    /**
     * Função responsável por deslogar o usuário, removendo o
     * token de acesso do DataStore
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.TOKEN] = ""
        }
    }

}