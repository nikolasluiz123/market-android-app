package br.com.market.localdataaccess.tuples

import br.com.market.enums.EnumUnit
import java.util.*

data class ProductImageTuple(
    val productId: UUID,
    val productName: String,
    val productPrice: Double,
    val productQuantity: Int,
    val productQuantityUnit: EnumUnit,
    val categoryBrandId: UUID,
    val productActive: Boolean,
    val imageBytes: ByteArray?,
    val imageUrl: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImageTuple

        if (productId != other.productId) return false

        return true
    }

    override fun hashCode(): Int {
        return productId.hashCode()
    }
}