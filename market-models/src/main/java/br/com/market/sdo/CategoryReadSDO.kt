package br.com.market.sdo

import br.com.market.sdo.base.MarketRestrictionSDO

data class CategoryReadSDO(
    override var localId: String,
    override var active: Boolean,
    override var marketId: Long?,
    var id: Long?,
    var name: String,
    var market: MarketSDO,
    var company: CompanySDO,
    var device: DeviceSDO,
    var user: UserSDO
) : MarketRestrictionSDO()