package br.com.market.core.filter

import br.com.market.core.filter.formatter.IAdvancedFilterFormatter

data class CommonAdvancedFilterItem(
    val labelResId: Int,
    var filterType: EnumAdvancedFilterType,
    val formatter: IAdvancedFilterFormatter,
    val labelsReference: Int? = null,
    var checked: Boolean = false,
    val enabled: Boolean = true,
    val visible: Boolean = true,
    var value: Any? = null
)