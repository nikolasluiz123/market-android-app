package br.com.market.core.callbacks

fun interface IServiceOperationCallback {
   fun onExecute(onSuccess: () -> Unit, onError: (message: String) -> Unit)
}