package br.com.market.domain

import androidx.room.ColumnInfo
import br.com.market.domain.base.BaseDomain
import br.com.market.enums.EnumUnit
import java.util.*

data class ProductDomain(
    override var id: UUID? = null,
    override var active: Boolean = true,
    @ColumnInfo(name = "company_id")
    override var companyId: UUID? = null,
    override var synchronized: Boolean = false,
    var name: String? = null,
    var price: Double? = null,
    var quantity: Int? = null,
    var unit: EnumUnit? = null,
    var images: MutableList<ByteArray>
): BaseDomain()