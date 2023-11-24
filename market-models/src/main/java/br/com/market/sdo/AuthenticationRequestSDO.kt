package br.com.market.sdo

/**
 * Classe utilizada para solicitar a autenticação do usuário ao serviço.
 *
 * @property email Email do usuário
 * @property password Senha do usuário.
 *
 * @author Nikolas Luiz Schmitt
 */
data class AuthenticationRequestSDO(
    var email: String = "",
    var password: String = "",
    var tempDeviceId: String = ""
)