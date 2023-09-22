package br.com.market.domain

import br.com.market.domain.base.BaseDomain

data class ClientDomain(
    var userDomain: UserDomain,
    var addressDomain: AddressDomain,
    var cpf: String,
    override var id: String? = null,
    override var active: Boolean = true,
    override var synchronized: Boolean = false,
): BaseDomain()