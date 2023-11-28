package br.com.market.core.callbacks

import br.com.market.core.enums.EnumDialogType

fun interface IShowDialogCallback {
    fun onShow(type: EnumDialogType, message: String, onConfirm: () -> Unit, onCancel: () -> Unit)
}