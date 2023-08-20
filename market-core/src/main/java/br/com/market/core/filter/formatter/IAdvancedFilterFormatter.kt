package br.com.market.core.filter.formatter

interface IAdvancedFilterFormatter {

    fun formatToString(value: Any?): String

    fun formatToValue(text: String): Any? = null
}
