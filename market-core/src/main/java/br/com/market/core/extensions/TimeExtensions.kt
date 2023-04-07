package br.com.market.core.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDateTime.formatShort(): String {
    return this.format(FormatStyle.SHORT, FormatStyle.SHORT)
}

fun LocalDateTime.format(dateFormatStyle: FormatStyle, timeFormatStyle: FormatStyle): String {
    return this.format(DateTimeFormatter.ofLocalizedDateTime(dateFormatStyle, timeFormatStyle))
}