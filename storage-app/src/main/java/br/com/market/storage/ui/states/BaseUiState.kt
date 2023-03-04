package br.com.market.storage.ui.states

abstract class BaseUiState {
    abstract val serverError: String
    abstract val showErrorDialog: Boolean
    abstract val onToggleErrorDialog: (String) -> Unit

    abstract val showLoading: Boolean
    abstract val onToggleLoading: () -> Unit

    abstract val onValidate: () -> Boolean
}