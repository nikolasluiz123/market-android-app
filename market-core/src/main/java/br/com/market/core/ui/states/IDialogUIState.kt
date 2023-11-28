package br.com.market.core.ui.states

import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType

interface IDialogUIState {
    val dialogType: EnumDialogType
    val dialogMessage: String
    val showDialog: Boolean
    val onShowDialog: IShowDialogCallback?
    val onHideDialog: () -> Unit
    val onConfirm: () -> Unit
    val onCancel: () -> Unit
    var internalErrorMessage: String
}