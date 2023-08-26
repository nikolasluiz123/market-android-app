package br.com.market.localdataaccess.filter

import br.com.market.enums.EnumOperationType
import java.time.LocalDateTime

class MovementSearchScreenFilters(
    var productName: String? = null,
    var description: String? = null,
    var datePrevision: Pair<LocalDateTime?, LocalDateTime?>? = null,
    var dateRealization: Pair<LocalDateTime?,LocalDateTime?>? = null,
    var operationType: EnumOperationType? = null,
    var quantity: Long? = null,
    var responsible: Pair<String, String>? = null
)