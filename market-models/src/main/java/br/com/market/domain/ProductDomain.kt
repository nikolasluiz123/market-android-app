package br.com.market.domain

import androidx.room.ColumnInfo
import br.com.market.domain.base.MarketRestrictionDomain
import br.com.market.enums.EnumUnit

data class ProductDomain(
    override var id: String? = null,
    override var active: Boolean = true,
    @ColumnInfo(name = "market_id")
    override var marketId: Long? = null,
    override var synchronized: Boolean = false,
    var name: String? = null,
    var price: Double? = null,
    var quantity: Double? = null,
    var quantityUnit: EnumUnit? = null,
    var images: MutableList<ProductImageDomain>
): MarketRestrictionDomain()