package br.com.market.core.filter

import java.time.LocalDateTime

open class DateAdvancedFilterArgs(
    val titleResId: Int,
    val value: Pair<LocalDateTime?, LocalDateTime?>? = null
)
