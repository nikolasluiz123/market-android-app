package br.com.market.domain

import java.util.*

data class CategoryDomain(
    var id: UUID? = null,
    var name: String = "",
    var active: Boolean = true,
)