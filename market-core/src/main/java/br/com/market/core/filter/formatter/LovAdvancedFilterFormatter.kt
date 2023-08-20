package br.com.market.core.filter.formatter

class LovAdvancedFilterFormatter: IAdvancedFilterFormatter {

    override fun formatToString(value: Any?): String = (value as Pair<String, String>?)?.second ?: ""
}