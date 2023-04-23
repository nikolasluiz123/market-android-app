package br.com.market.core.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Função para formatar uma data com hora exibindo
 * dia, mês, ano, horas e minutos.
 *
 * Exemplo: 32/04/2023 10:23
 *
 * @author Nikolas Luiz Schmitt
 */
fun LocalDateTime.formatShort(): String {
    return this.format(FormatStyle.SHORT, FormatStyle.SHORT)
}

/**
 * Função que pode ser utilizada para formatar qualquer LocalDateTime
 * podendo escolher a formatação da data e da hora de forma distinta.
 *
 * @param dateFormatStyle Formato da data
 * @param timeFormatStyle Formato da hora
 *
 * @author Nikolas Luiz Schmitt
 */
fun LocalDateTime.format(dateFormatStyle: FormatStyle, timeFormatStyle: FormatStyle): String {
    return this.format(DateTimeFormatter.ofLocalizedDateTime(dateFormatStyle, timeFormatStyle))
}