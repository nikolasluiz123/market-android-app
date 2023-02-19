package br.com.market.storage.business.dao.tuples

data class BrandTuple(
    val brandId: Long,
    var completeProductName: String,
    var count: Int
)