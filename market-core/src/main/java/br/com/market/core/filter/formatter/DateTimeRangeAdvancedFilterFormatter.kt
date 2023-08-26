package br.com.market.core.filter.formatter

import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.format
import java.time.LocalDateTime

class DateTimeRangeAdvancedFilterFormatter : IAdvancedFilterFormatter {

    @Suppress("UNCHECKED_CAST")
    override fun formatToString(value: Any?): String? {
        value as Pair<LocalDateTime?, LocalDateTime?>?

        return when {
            value?.first != null && value?.second != null -> {
                "${value!!.first!!.format(EnumDateTimePatterns.DATE_TIME)} - ${value.second!!.format(EnumDateTimePatterns.DATE_TIME)}"
            }

            value?.first != null -> {
                value!!.first!!.format(EnumDateTimePatterns.DATE_TIME)
            }

            value?.second != null -> {
                value!!.second!!.format(EnumDateTimePatterns.DATE_TIME)
            }

            else -> null
        }
    }
}