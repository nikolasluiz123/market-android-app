package br.com.market.servicedataaccess.responses.types

import java.net.HttpURLConnection

/**
 * Uma classe específica para a resposta de uma autenticação ou
 * registro de um usuário no serviço.
 *
 * @property token Token gerado pelo serviço que será usado em todas
 * as chamadas dos end points que necessitam de autenticação.
 *
 * @author Nikolas Luiz Schmitt
 */
data class AuthenticationResponse(
    var token: String? = null,
    var userLocalId: String? = null,
    override var code: Int,
    override var success: Boolean = false,
    override var error: String? = null
): IMarketServiceResponse {

    fun getObjectSynchronized() = success && code != HttpURLConnection.HTTP_UNAVAILABLE
}
