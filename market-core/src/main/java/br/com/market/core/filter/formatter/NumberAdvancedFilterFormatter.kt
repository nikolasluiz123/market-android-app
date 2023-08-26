package br.com.market.core.filter.formatter

import java.text.DecimalFormat

class NumberAdvancedFilterFormatter(
    private val integer: Boolean,
    private val minFractionDigits: Int = 2,
    private val maxFractionDigits: Int = 2
) : IAdvancedFilterFormatter {

    override fun formatToString(value: Any?): String? {
        return if (integer) {
            (value as Long?)?.toString()
        } else {
            (value as Double?)?.let {
                DecimalFormat.getNumberInstance().run {
                    minimumFractionDigits = minFractionDigits
                    maximumFractionDigits = maxFractionDigits
                    format(it)
                }
            }
        }
    }
}