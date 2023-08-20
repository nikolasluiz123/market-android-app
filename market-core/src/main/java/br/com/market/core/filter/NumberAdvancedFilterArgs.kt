package br.com.market.core.filter

class NumberAdvancedFilterArgs(
    val integer: Boolean,
    titleResId: Int,
    value: String = ""
) : AdvancedFilterArgs(titleResId, value)