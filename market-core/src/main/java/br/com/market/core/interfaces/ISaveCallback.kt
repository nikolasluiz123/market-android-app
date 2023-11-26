package br.com.market.core.interfaces

fun interface ISaveCallback {
   fun onSave(onSuccess: () -> Unit, onError: (message: String) -> Unit)
}