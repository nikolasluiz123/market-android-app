package br.com.market.core.ui.states

interface ILoadingUIState {
    val showLoading: Boolean
    val onToggleLoading: () -> Unit
}