package br.com.market.core.extensions

import br.com.market.core.inputs.CommonAdvancedFilterItem
import br.com.market.core.inputs.values.FilterValue
import java.time.LocalDateTime

fun CommonAdvancedFilterItem.getStringFilterValue(): FilterValue<String?> {
    return FilterValue(this.checked, this.value as String?)
}

@Suppress("UNCHECKED_CAST")
fun CommonAdvancedFilterItem.getDateRangeFilterValue(): FilterValue<Pair<LocalDateTime?, LocalDateTime?>?> {
    return FilterValue(this.checked, this.value as Pair<LocalDateTime?, LocalDateTime?>?)
}

@Suppress("UNCHECKED_CAST")
fun <T> CommonAdvancedFilterItem.getEnumFilterValue(enumValues: Array<T>): FilterValue<T?> {
    return FilterValue(this.checked, (this.value as Pair<String, Int>?)?.second?.let { enumValues[it] })
}

fun CommonAdvancedFilterItem.getLongFilterValue(): FilterValue<Long?> {
    return FilterValue(this.checked, this.value as Long?)
}

@Suppress("UNCHECKED_CAST")
fun CommonAdvancedFilterItem.getLovPairFilterValue(): FilterValue<Pair<String, String?>?> {
    return FilterValue(this.checked, this.value as Pair<String, String?>?)
}