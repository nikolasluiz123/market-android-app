package br.com.market.storage.business.sdo.brand

data class NewBrandSDO(
    var localProductId: Long? = null,
    var localBrandId: Long? = null,
    var name: String,
    var count: Int = 0
)
