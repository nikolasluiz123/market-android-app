package br.com.market.storage.business.sdo.brand

data class UpdateStorageSDO(
    var productId: Long,
    var brandId: Long,
    var count: Int
)