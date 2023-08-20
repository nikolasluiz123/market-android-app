package br.com.market.core.ui.states.filter

data class NumberAdvancedFilterUIState(
    var titleResId: Int? = null,
    var value: String = "",
    var integer: Boolean = true,
    var onValueChange: (String) -> Unit = { }
)
