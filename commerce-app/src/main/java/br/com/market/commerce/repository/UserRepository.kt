package br.com.market.commerce.repository

import br.com.market.domain.UserDomain
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.servicedataaccess.webclients.UserWebClient
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val webClient: UserWebClient
) {

    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        return webClient.authenticate(userDomain = userDomain)
    }
}