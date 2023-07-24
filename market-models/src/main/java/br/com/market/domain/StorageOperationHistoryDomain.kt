package br.com.market.domain

import br.com.market.domain.base.BaseDomain
import br.com.market.enums.EnumOperationType
import java.time.LocalDateTime

data class StorageOperationHistoryDomain(
    override var id: String? = null,
    override var active: Boolean = true,
    override var marketId: Long? = null,
    override var synchronized: Boolean = false,
    var productId: String? = null,
    var quantity: Int = 0,
    var dateRealization: LocalDateTime? = null,
    var datePrevision: LocalDateTime? = null,
    var operationType: EnumOperationType? = null,
    var description: String? = null,
    var userId: String? = null
) : BaseDomain()
