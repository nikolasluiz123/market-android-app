package br.com.market.core.filter.formatter

open class StringAdvancedFilterFormatter : IAdvancedFilterFormatter {

    override fun formatToString(value: Any?): String {
        return value?.toString() ?: ""
    }
}