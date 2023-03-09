package br.com.market.storage.extensions

fun String?.navParamToLong(): Long? = this?.replace("}", "")?.replace("{", "")?.toLong()

fun String.navParamToLong(): Long = this.replace("}", "").replace("{", "").toLong()