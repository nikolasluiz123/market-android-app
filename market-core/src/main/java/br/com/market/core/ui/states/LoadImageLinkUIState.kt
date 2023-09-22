package br.com.market.core.ui.states

data class LoadImageLinkUIState(
    val link: String = "",
    val onLinkChange: (String) -> Unit = { },
    val linkErrorMessage: String = "",
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = {},
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState, ILoadingUIState, IServerErrorHandlerUIState