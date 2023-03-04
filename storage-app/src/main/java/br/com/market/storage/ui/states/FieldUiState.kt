package br.com.market.storage.ui.states

data class FieldUiState(
    val value: String = "",
    val errorMessage: String = "",
    val onValueChange: () -> Unit = { }
) {
    fun isNotBlankValue() = value.isNotBlank()
}