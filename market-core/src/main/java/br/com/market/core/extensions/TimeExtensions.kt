package br.com.market.core.extensions

import br.com.market.core.enums.EnumDateTimePatterns
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.parseToLocalDate(enumDateTimePatterns: EnumDateTimePatterns): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun String.parseToLocalTime(enumDateTimePatterns: EnumDateTimePatterns): LocalTime {
    return LocalTime.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun String.parseToLocalDateTime(enumDateTimePatterns: EnumDateTimePatterns): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun LocalDate.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun LocalTime.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
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
fun LocalDateTime.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}