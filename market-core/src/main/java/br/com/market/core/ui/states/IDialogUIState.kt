package br.com.market.core.ui.states

import br.com.market.core.enums.EnumDialogType
import br.com.market.core.interfaces.IShowDialog

interface IDialogUIState {
    val dialogType: EnumDialogType
    val dialogMessage: String
    val showDialog: Boolean
    val onShowDialog: IShowDialog?
    val onHideDialog: () -> Unit
    val onConfirm: () -> Unit
    val onCancel: () -> Unit
}