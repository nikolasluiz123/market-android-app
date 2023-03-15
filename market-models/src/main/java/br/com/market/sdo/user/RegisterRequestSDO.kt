package br.com.market.sdo.user

/**
 * Classe utilizada para solicitar o cadastro do usuário ao serviço.
 *
 * @property name Nome do usuário.
 * @property email E-mail do usuário.
 * @property password Senha do usuário.
 *
 * @author Nikolas Luiz Schmitt
 */
data class RegisterRequestSDO(
    var name: String = "",
    var email: String = "",
    var password: String = ""
)