package br.com.market.domain

import br.com.market.domain.base.BaseDomain
import br.com.market.enums.EnumUnit

data class ProductImageReadDomain(
    override var id: String?,
    override var active: Boolean,
    override var synchronized: Boolean,
    val productName: String,
    val productPrice: Double,
    val productQuantity: Double,
    val productQuantityUnit: EnumUnit,
    val categoryBrandId: String,
    val imageBytes: ByteArray?,
    val imageUrl: String?,
): BaseDomain() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImageReadDomain

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}