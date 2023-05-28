package br.com.market.localdataaccess.tuples

import br.com.market.enums.EnumUnit

data class ProductImageTuple(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productQuantity: Double,
    val productQuantityUnit: EnumUnit,
    val categoryBrandId: String,
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