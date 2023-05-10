package br.com.market.core.extensions

import java.text.DecimalFormat

/**
 * Função para converter um ID enviado por navegação para um Long.
 *
 * É uma implementação para não nullables.
 *
 * @author Nikolas Luiz Schmitt
 */
fun String.navParamToString(): String = this.replace("}", "").replace("{", "")

fun String.parseToDouble(): Double? {
    val formatter = DecimalFormat.getInstance()
    return formatter.parse(this)?.toDouble()
}