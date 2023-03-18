package br.com.market.sdo.product

import java.util.*

data class SyncProductDTO(
    var localProductId: UUID,
    var name: String,
    var imageUrl: String
)