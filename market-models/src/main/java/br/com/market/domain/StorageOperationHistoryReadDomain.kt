package br.com.market.domain

import br.com.market.enums.EnumOperationType
import java.time.LocalDateTime

data class StorageOperationHistoryReadDomain(
    val id: String,
    val categoryId: String,
    val brandId: String,
    val productId: String,
    val productName: String,
    val dateRealization: LocalDateTime?,
    val datePrevision: LocalDateTime?,
    val quantity: Int,
    val responsibleId: String?,
    val responsibleName: String?,
    val description: String?,
    val operationType: EnumOperationType
)
