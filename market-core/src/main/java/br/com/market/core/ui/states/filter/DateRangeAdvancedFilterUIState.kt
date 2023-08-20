package br.com.market.core.ui.states.filter

data class DateRangeAdvancedFilterUIState(
    var titleResId: Int? = null,
    var onDateFromChange: (String) -> Unit = { },
    var onTimeFromChange: (String) -> Unit = { },
    var onDateToChange: (String) -> Unit = { },
    var onTimeToChange: (String) -> Unit = { },
    var dateFrom: String = "",
    var timeFrom: String = "",
    var dateTo: String = "",
    var timeTo: String = ""
)
