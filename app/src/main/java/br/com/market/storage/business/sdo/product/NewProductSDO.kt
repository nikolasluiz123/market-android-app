package br.com.market.storage.business.sdo.product

import br.com.market.storage.business.sdo.brand.NewBrandSDO

data class NewProductSDO(
    var id: Long? = null,
    var name: String,
    var imageUrl: String = "",
    var brands: List<NewBrandSDO>
)