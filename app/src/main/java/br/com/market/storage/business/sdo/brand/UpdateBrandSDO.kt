package br.com.market.storage.business.sdo.brand

data class UpdateBrandSDO(
    var id: Long?,
    var name: String,
    var sumCount: Int = 0
)
