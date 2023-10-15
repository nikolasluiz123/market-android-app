package br.com.market.sdo

import br.com.market.sdo.base.MarketRestrictionSDO

data class BrandReadSDO(
    override var localId: String,
    override var active: Boolean,
    override var marketId: Long?,
    var id: Long?,
    var name: String,
    var category: CategorySDO,
    var categoryBrand: CategoryBrandSDO,
    var market: MarketSDO,
    var company: CompanySDO
) : MarketRestrictionSDO()