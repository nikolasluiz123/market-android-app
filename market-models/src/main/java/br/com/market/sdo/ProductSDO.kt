package br.com.market.sdo

import br.com.market.enums.EnumUnit
import br.com.market.sdo.base.MarketRestrictionSDO

data class ProductSDO(
    override var localId: String,
    override var marketId: Long? = null,
    override var active: Boolean = true,
    var name: String? = null,
    var price: Double? = null,
    var quantity: Double? = null,
    var quantityUnit: EnumUnit? = null,
    var categoryBrandLocalId: String? = null
): MarketRestrictionSDO()