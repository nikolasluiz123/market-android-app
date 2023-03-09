package br.com.market.storage.business.sdo.product

data class UpdateProductSDO(
    var id: Long? = null,
    var idLocal: Long? = null,
    var name: String,
    var imageUrl: String = ""
)