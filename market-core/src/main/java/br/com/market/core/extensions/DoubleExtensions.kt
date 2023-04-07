package br.com.market.core.extensions

import java.text.DecimalFormat

fun Double.formatToCurrency(): String {
    val formatter = DecimalFormat.getCurrencyInstance()
    return formatter.format(this)
}