package br.com.market.core.extensions

import br.com.market.core.filter.CommonAdvancedFilterItem
import br.com.market.core.filter.values.FilterValue
import java.time.LocalDateTime

fun CommonAdvancedFilterItem.getStringFilterValue(): FilterValue<String?> {
    return FilterValue(this.checked, this.value as String?)
}

fun CommonAdvancedFilterItem.getDateRangeFilterValue(): FilterValue<Pair<LocalDateTime?, LocalDateTime?>?> {
    return FilterValue(this.checked, this.value as Pair<LocalDateTime?, LocalDateTime?>?)
}

fun <T> CommonAdvancedFilterItem.getEnumFilterValue(enumValues: Array<T>): FilterValue<T?> {
    return FilterValue(this.checked, (this.value as Pair<String, Int>?)?.second?.let { enumValues[it] })
}

fun CommonAdvancedFilterItem.getLongFilterValue(): FilterValue<Long?> {
    return FilterValue(this.checked, this.value as Long?)
}

fun CommonAdvancedFilterItem.getLovPairFilterValue(): FilterValue<Pair<String, String?>?> {
    return FilterValue(this.checked, this.value as Pair<String, String?>?)
}