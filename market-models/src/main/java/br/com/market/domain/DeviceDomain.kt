package br.com.market.domain

import br.com.market.domain.base.MarketRestrictionDomain

data class DeviceDomain(
    override var id: String? = null,
    override var active: Boolean = true,
    override var marketId: Long? = null,
    override var synchronized: Boolean = false,
    var name: String? = null
): MarketRestrictionDomain()