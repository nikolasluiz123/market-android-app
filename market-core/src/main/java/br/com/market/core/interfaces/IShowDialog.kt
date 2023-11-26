package br.com.market.core.interfaces

import br.com.market.core.enums.EnumDialogType

fun interface IShowDialog {
    fun onShow(type: EnumDialogType, message: String, onConfirm: () -> Unit, onCancel: () -> Unit)
}