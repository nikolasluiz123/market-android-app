package br.com.market.core.extensions

import br.com.market.core.enums.EnumDateTimePatterns
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Converte uma string em um objeto LocalDate utilizando o padrão especificado.
 *
 * @param enumDateTimePatterns O padrão de formato para a conversão.
 * @return O objeto LocalDate resultante da conversão.
 * @receiver A string contendo a data a ser convertida.
 * @author Nikolas Luiz Schmitt
 */
fun String.parseToLocalDate(enumDateTimePatterns: EnumDateTimePatterns): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

/**
 * Converte uma string em um objeto LocalTime utilizando o padrão especificado.
 *
 * @param enumDateTimePatterns O padrão de formato para a conversão.
 * @return O objeto LocalTime resultante da conversão.
 * @receiver A string contendo a hora a ser convertida.
 * @author Nikolas Luiz Schmitt
 */
fun String.parseToLocalTime(enumDateTimePatterns: EnumDateTimePatterns): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

/**
 * Converte uma string em um objeto LocalDateTime utilizando o padrão especificado.
 *
 * @param enumDateTimePatterns O padrão de formato para a conversão.
 * @return O objeto LocalDateTime resultante da conversão.
 * @receiver A string contendo a data e hora a ser convertida.
 * @throws IndexOutOfBoundsException em caso de falta de informações necessárias.
 * @throws Exception em outros erros.
 * @author Nikolas Luiz Schmitt
 */
fun String.parseToLocalDateTime(enumDateTimePatterns: EnumDateTimePatterns): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun String.formatFileDateToDateTime(): String {
    return parseToLocalDateTime(EnumDateTimePatterns.DATE_TIME_FILE_NAME).format(EnumDateTimePatterns.DATE_TIME)
}

/**
 * Formata um objeto LocalDate em uma string utilizando o padrão especificado.
 *
 * @param enumDateTimePatterns O padrão de formato para a formatação.
 * @return A string formatada resultante.
 * @receiver O objeto LocalDate a ser formatado.
 */
fun LocalDate.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

/**
 * Formata um objeto LocalTime em uma string utilizando o padrão especificado.
 *
 * @param enumDateTimePatterns O padrão de formato para a formatação.
 * @return A string formatada resultante.
 * @receiver O objeto LocalTime a ser formatado.
 */
fun LocalTime.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

/**
 * Formata um objeto LocalDateTime em uma string utilizando o padrão especificado.
 *
 * @param enumDateTimePatterns O padrão de formato para a formatação.
 * @return A string formatada resultante.
 * @receiver O objeto LocalDateTime a ser formatado.
 * @author Nikolas Luiz Schmitt
 */
fun LocalDateTime.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}