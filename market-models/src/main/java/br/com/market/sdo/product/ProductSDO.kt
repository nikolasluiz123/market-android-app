package br.com.market.sdo.product

import br.com.market.enums.EnumUnit
import br.com.market.sdo.base.BaseSDO

data class ProductSDO(
    override var localId: String,
    override var companyId: Long? = null,
    override var active: Boolean = true,
    var name: String? = null,
    var price: Double? = null,
    var quantity: Double? = null,
    var quantityUnit: EnumUnit? = null,
    var categoryBrandLocalId: String? = null
): BaseSDO()