package br.com.market.domain

import br.com.market.domain.base.MarketRestrictionDomain

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
    override var id: String? = null,
    override var active: Boolean = true,
    override var marketId: Long? = null,
    override var synchronized: Boolean = false,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var tempDeviceId: String = ""
): MarketRestrictionDomain()