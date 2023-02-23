package br.com.market.storage.ui.domains

data class ProductBrandDomain(
    val brandId: Long? = null,
    val productName: String = "",
    val brandName: String = "",
    val count: Int = 0
)