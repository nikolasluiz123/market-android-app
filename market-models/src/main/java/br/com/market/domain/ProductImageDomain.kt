package br.com.market.domain

import br.com.market.domain.base.MarketRestrictionDomain

data class ProductImageDomain(
    override var id: String? = null,
    override var active: Boolean = true,
    override var marketId: Long? = null,
    override var synchronized: Boolean = false,
    var byteArray: ByteArray? = null,
    val productId: String? = null,
    var principal: Boolean = false,
) : MarketRestrictionDomain() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImageDomain

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
