package br.com.market.storage.ui.states

abstract class BaseUiState {
    abstract val serverMessage: String
    abstract val showDialogMessage: Boolean
    abstract val onToggleMessageDialog: (String) -> Unit

    abstract val showLoading: Boolean
    abstract val onToggleLoading: () -> Unit

    abstract val onValidate: () -> Boolean
}