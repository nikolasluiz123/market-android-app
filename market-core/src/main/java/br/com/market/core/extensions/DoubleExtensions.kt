package br.com.market.core.extensions

import java.text.DecimalFormat

/**
 * Função para formatar um double no padrão de
 * valores monetários de acordo com as configurações
 * do dispositivo.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Double.formatToCurrency(): String {
    val formatter = DecimalFormat.getCurrencyInstance()
    return formatter.format(this)
}

/**
 * Função que pode ser utilizada para formatar
 * um valor Double para um valor decimal no padrão
 * correto de acordo com o Locale.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Double.format(): String {
    val formatter = DecimalFormat.getInstance()
    return formatter.format(this)
}