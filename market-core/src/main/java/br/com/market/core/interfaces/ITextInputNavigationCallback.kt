package br.com.market.core.interfaces

import br.com.market.core.inputs.arguments.InputArgs

fun interface ITextInputNavigationCallback {
    fun onNavigate(args: InputArgs, callback: (value: String?) -> Unit)
}