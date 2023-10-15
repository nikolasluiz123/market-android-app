package br.com.market.domain

import br.com.market.domain.base.BaseDomain

data class MarketLovDomain(
    var name: String,
    var completeAddress: String,
    override var id: String? = null,
    override var active: Boolean = true,
    override var synchronized: Boolean = true,
): BaseDomain()
