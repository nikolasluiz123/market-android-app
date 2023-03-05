package br.com.market.storage.business.sdo.product

data class NewProductSDO(
    var id: Long? = null,
    var name: String,
    var imageUrl: String = ""
)