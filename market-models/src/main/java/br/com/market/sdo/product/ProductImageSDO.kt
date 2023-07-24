package br.com.market.sdo.product

import br.com.market.sdo.base.BaseSDO

data class ProductImageSDO(
    override var localId: String,
    override var marketId: Long? = null,
    override var active: Boolean = true,
    var bytes: ByteArray? = null,
    var imageUrl: String? = null,
    var productLocalId: String? = null,
    var principal: Boolean = false
): BaseSDO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImageSDO

        if (localId != other.localId) return false

        return true
    }

    override fun hashCode(): Int {
        return localId.hashCode()
    }
}