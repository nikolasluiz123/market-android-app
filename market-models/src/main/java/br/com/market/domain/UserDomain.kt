package br.com.market.domain

/**
 * Classe de domínio do usuário.
 *
 * @property id Identificador do usuário.
 * @property name Nome do usuário.
 * @property email E-mail do usuário.
 * @property password Senha do usuário.
 *
 * @author Nikolas Luiz Schmitt
 */
data class UserDomain(
    var id: Long? = null,
    var name: String = "",
    var email: String = "",
    var password: String = ""
)