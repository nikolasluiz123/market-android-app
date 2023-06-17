package br.com.market.core.extensions

import java.text.DecimalFormat

/**
 * Função para converter um ID enviado por navegação para um Long.
 *
 * É uma implementação para não nullables.
 *
 * @author Nikolas Luiz Schmitt
 */
fun String?.navParamToString(): String? {
    val value = this?.replace("}", "")?.replace("{", "")
    return if (value == "null") null else value
}

/**
 * Função que pode ser utilizada para converter uma string
 * em um valor decimal no padrão do locale.
 *
 * @author Nikolas Luiz Schmitt
 */
fun String.parseToDouble(): Double? {
    val formatter = DecimalFormat.getInstance()
    return formatter.parse(this)?.toDouble()
}