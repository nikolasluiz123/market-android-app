package br.com.market.domain

import androidx.room.ColumnInfo
import java.util.*

data class CategoryDomain(
    var id: UUID? = null,
    var name: String = "",
    var active: Boolean = true,
    @ColumnInfo(name = "company_id")
    var companyId: UUID? = null,
    var synchronized: Boolean = false
)