package br.com.market.storage.business.services.response

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
    override var code: Int,
    override var success: Boolean = false,
    override var error: String? = null
): IMarketServiceResponse
