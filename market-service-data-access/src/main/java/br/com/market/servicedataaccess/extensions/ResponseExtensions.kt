package br.com.market.servicedataaccess.extensions

import br.com.market.servicedataaccess.responses.AuthenticationResponse
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

/**
 * Função que pode ser utilizada para recuperar a resposta de uma requisição
 * feita ao serviço.
 *
 * Foi implementada para ser utilizada especificamente em uma resposta de persistência.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Response<PersistenceResponse>.getPersistenceResponseBody(): PersistenceResponse {
    val type = object : TypeToken<PersistenceResponse>() {}.type
    return this.body() ?: Gson().fromJson(this.errorBody()!!.charStream(), type)
}

/**
 * Função que pode ser utilizada para recuperar a resposta de uma requisição
 * feita ao serviço.
 *
 * Foi implementada para ser utilizada especificamente em uma resposta de autenticação.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Response<AuthenticationResponse>.getAuthenticationResponseBody(): AuthenticationResponse {
    val type = object : TypeToken<AuthenticationResponse>() {}.type
    return this.body() ?: Gson().fromJson(this.errorBody()!!.charStream(), type)
}

/**
 * Função que pode ser utilizada para recuperar a resposta de uma requisição
 * feita ao serviço.
 *
 * Foi implementada para ser utilizada em qualquer tipo de resposta, para casos
 * em que não há uma resposta específica.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Response<MarketServiceResponse>.getResponseBody(): MarketServiceResponse {
    val type = object : TypeToken<MarketServiceResponse>() {}.type
    return this.body() ?: Gson().fromJson(this.errorBody()!!.charStream(), type)
}