package br.com.market.core.callbacks

fun interface ISaveCallback {
   fun onSave(onSuccess: () -> Unit, onError: (message: String) -> Unit)
}