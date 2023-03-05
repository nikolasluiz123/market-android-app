package br.com.market.storage.business.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import br.com.market.storage.business.services.response.AuthenticationResponse
import br.com.market.storage.business.webclient.UserWebClient
import br.com.market.storage.preferences.PreferencesKey
import br.com.market.storage.preferences.dataStore
import br.com.market.storage.ui.domains.UserDomain
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val context: Context,
    private val userWebClient: UserWebClient
) {

    suspend fun registerUser(userDomain: UserDomain): AuthenticationResponse {
        return userWebClient.registerUser(userDomain)
    }

    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        val response = userWebClient.authenticate(userDomain = userDomain)

        if (response.success) {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKey.TOKEN] = response.token!!
            }
        }

        return response
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.TOKEN] = ""
        }
    }

}