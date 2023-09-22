package br.com.market.domain

import br.com.market.domain.base.BaseDomain

data class AddressDomain(
    var cep: String,
    var state: String,
    var city: String,
    var publicPlace: String,
    var number: String,
    var complement: String? = null,
    override var id: String? = null,
    override var active: Boolean = true,
    override var synchronized: Boolean = false
) : BaseDomain()