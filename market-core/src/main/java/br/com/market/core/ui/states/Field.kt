package br.com.market.core.ui.states

data class Field(
    val value: String = "",
    val onChange: (String) -> Unit = { },
    val errorMessage: String = ""
) {

    fun valueIsEmpty() = value.isEmpty()
}