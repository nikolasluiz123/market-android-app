package br.com.market.sdo.category

import java.util.*

data class CategorySDO(
    var localCategoryId: UUID,
    var name: String? = null,
    var companyId: Long? = null,
    var active: Boolean = true
)