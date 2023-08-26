package br.com.market.core.ui.states.filter

data class TextAdvancedFilterUIState(
    var titleResId: Int? = null,
    var value: String? = null,
    var onValueChange: (String) -> Unit = { }
)
