package br.com.market.sdo

import br.com.market.enums.EnumUnit
import br.com.market.sdo.base.MarketRestrictionSDO

data class ProductClientSDO(
    override var localId: String,
    override var active: Boolean,
    override var marketId: Long?,
    var name: String,
    var price: Double,
    var quantity: Double,
    var quantityUnit: EnumUnit,
    var image: ProductImageSDO,
    var categoryBrand: CategoryBrandSDO,
    var category: CategorySDO,
    var brand: BrandSDO,
    var market: MarketSDO,
    var company: CompanySDO,
    var id: Long? = null,
): MarketRestrictionSDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductClientSDO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
