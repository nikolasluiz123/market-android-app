package br.com.market.sdo

import br.com.market.sdo.base.MarketRestrictionSDO

data class CategoryBrandSDO(
    override var localId: String,
    override var marketId: Long? = null,
    override var active: Boolean = true,
    var localCategoryId: String,
    var localBrandId: String
): MarketRestrictionSDO()