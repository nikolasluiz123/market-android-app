package br.com.market.core.ui.states

interface IServerErrorHandlerUIState {
    val serverMessage: String
    val showDialogMessage: Boolean
    val onToggleMessageDialog: (String) -> Unit
}