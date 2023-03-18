package br.com.market.sdo.brand

import java.util.*

data class SyncProductBrandSDO(
    var localId: UUID,
    var localProductId: UUID,
    var localBrandId: UUID,
    var count: Int
)