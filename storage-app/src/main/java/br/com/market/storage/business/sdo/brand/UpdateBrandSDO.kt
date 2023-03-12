package br.com.market.storage.business.sdo.brand

data class UpdateBrandSDO(
    var localBrandId: Long,
    var name: String,
    var count: Int = 0
)
