package br.com.market.sdo.product

import java.util.*

data class CountRegistersSDO(
    var productLocalIds: List<UUID>,
    var brandLocalIds: List<UUID>,
    var productBrandLocalIds: List<UUID>
)