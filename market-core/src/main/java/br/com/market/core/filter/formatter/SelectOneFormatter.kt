package br.com.market.core.filter.formatter

class SelectOneFormatter: IAdvancedFilterFormatter {

    @Suppress("UNCHECKED_CAST")
    override fun formatToString(value: Any?): String? {
        return (value as Pair<String, Int>?)?.first
    }
}