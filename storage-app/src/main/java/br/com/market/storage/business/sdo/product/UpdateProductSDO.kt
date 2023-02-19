package br.com.market.storage.business.sdo.product

import br.com.market.storage.business.sdo.brand.UpdateBrandSDO

data class UpdateProductSDO(
    var id: Long?,
    var name: String,
    var imageUrl: String = "",
    var brands: List<UpdateBrandSDO>
)