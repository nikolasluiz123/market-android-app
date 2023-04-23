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