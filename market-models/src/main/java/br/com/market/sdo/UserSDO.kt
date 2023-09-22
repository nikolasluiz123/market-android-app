package br.com.market.sdo

import br.com.market.sdo.base.MarketRestrictionSDO

/**
 * Classe utilizada para solicitar o cadastro do usuário ao serviço.
 *
 * @property name Nome do usuário.
 * @property email E-mail do usuário.
 * @property password Senha do usuário.
 *
 * @author Nikolas Luiz Schmitt
 */
data class UserSDO(
    override var localId: String,
    override var marketId: Long? = null,
    override var active: Boolean = true,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var token: String? = null
) : MarketRestrictionSDO()