package br.com.market.core.ui.states

import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType

data class LoadImageLinkUIState(
    val link: String = "",
    val onLinkChange: (String) -> Unit = { },
    val linkErrorMessage: String = "",
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false },
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val onShowDialog: IShowDialogCallback? = null,
    override val onHideDialog: () -> Unit = { },
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { },
    override var internalErrorMessage: String = ""
) : IValidationUIState, ILoadingUIState, IDialogUIState