package br.com.market.storage.ui.domains

data class ProductBrandDomain(
    var brandId: Long? = null,
    var productName: String = "",
    var brandName: String = "",
    var count: Int = 0
)