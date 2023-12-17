package br.com.market.localdataaccess.filter

import br.com.market.core.inputs.values.FilterValue
import java.time.LocalDateTime

class MovementSearchScreenFilters(
    var productName: FilterValue<String?> = FilterValue(),
    var description: FilterValue<String?> = FilterValue(),
    var datePrevision: FilterValue<Pair<LocalDateTime?, LocalDateTime?>?> = FilterValue(),
    var dateRealization: FilterValue<Pair<LocalDateTime?, LocalDateTime?>?> = FilterValue(),
    var operationType: FilterValue<Pair<String, Int>?> = FilterValue(),
    var quantity: FilterValue<Long?> = FilterValue(),
    var responsible: FilterValue<Pair<String, String?>?> = FilterValue()
)