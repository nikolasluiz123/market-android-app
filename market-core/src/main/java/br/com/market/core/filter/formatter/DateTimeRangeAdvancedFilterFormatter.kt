package br.com.market.core.filter.formatter

import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.format
import java.time.LocalDateTime

class DateTimeRangeAdvancedFilterFormatter : IAdvancedFilterFormatter {

    override fun formatToString(value: Any?): String {
        value as Pair<LocalDateTime, LocalDateTime>?

        return value?.let {
            "${value.first.format(EnumDateTimePatterns.DATE_TIME)} - ${value.second.format(EnumDateTimePatterns.DATE_TIME)}"
        } ?: ""
    }
}