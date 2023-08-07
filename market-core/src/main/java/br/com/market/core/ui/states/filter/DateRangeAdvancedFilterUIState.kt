package br.com.market.core.ui.states.filter

data class DateRangeAdvancedFilterUIState(
    var titleResId: Int? = null,
    var dateFrom: String = "",
    var timeFrom: String = "",
    var dateTo: String = "",
    var timeTo: String = ""
)
