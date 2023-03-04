package br.com.market.storage.business.webclient

import android.content.Context
import android.util.Log
import br.com.market.storage.R
import br.com.market.storage.business.sdo.user.AuthenticationRequestSDO
import br.com.market.storage.business.sdo.user.RegisterRequestSDO
import br.com.market.storage.business.services.UserService
import br.com.market.storage.business.services.response.AuthenticationResponse
import br.com.market.storage.ui.domains.UserDomain
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userService: UserService
) {

    suspend fun registerUser(userDomain: UserDomain): AuthenticationResponse {
        try {
            val response = userService.register(
                RegisterRequestSDO(
                    name = userDomain.name,
                    email = userDomain.email,
                    password = userDomain.password
                )
            )

            return response.body()!!
        } catch (e: SocketTimeoutException) {
            return AuthenticationResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                success = false,
                error = context.getString(R.string.register_user_socket_timeout_error_message)
            )
        }
    }

    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        try {
            val response = userService.authenticate(
                AuthenticationRequestSDO(
                    email = userDomain.email,
                    password = userDomain.password
                )
            )

            return response.body()!!

        } catch (e: SocketTimeoutException) {
            return AuthenticationResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                success = false,
                error = context.getString(R.string.authenticate_user_socket_timeout_error_message)
            )
        }
    }
}