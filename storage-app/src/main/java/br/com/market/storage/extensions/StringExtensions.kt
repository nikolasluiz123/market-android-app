package br.com.market.storage.extensions

fun String?.toLongNavParam(): Long? = this?.replace("}", "")?.replace("{", "")?.toLong()