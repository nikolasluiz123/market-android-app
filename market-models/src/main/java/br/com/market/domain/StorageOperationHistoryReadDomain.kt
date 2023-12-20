package br.com.market.domain

import br.com.market.domain.base.BaseDomain
import br.com.market.enums.EnumOperationType
import java.time.LocalDateTime

data class StorageOperationHistoryReadDomain(
    override var id: String?,
    override var active: Boolean,
    override var synchronized: Boolean,
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
): BaseDomain()
