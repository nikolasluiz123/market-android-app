package br.com.market.sdo.product

import br.com.market.enums.EnumUnit
import br.com.market.sdo.base.BaseSDO
import java.util.*

data class ProductSDO(
    override var localId: UUID,
    override var companyId: Long? = null,
    override var active: Boolean = true,
    var name: String? = null,
    var price: Double? = null,
    var quantity: Int? = null,
    var quantityUnit: EnumUnit? = null,
    var categoryBrandLocalId: UUID? = null
): BaseSDO()