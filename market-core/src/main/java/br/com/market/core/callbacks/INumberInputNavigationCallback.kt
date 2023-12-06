package br.com.market.core.callbacks

import br.com.market.core.inputs.arguments.InputNumberArgs

fun interface INumberInputNavigationCallback {
    fun onNavigate(args: InputNumberArgs, callback: (Number?) -> Unit)
}