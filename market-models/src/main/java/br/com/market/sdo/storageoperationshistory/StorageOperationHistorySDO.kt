package br.com.market.sdo.storageoperationshistory

import br.com.market.enums.EnumOperationType
import br.com.market.sdo.base.BaseSDO
import java.time.LocalDateTime

data class StorageOperationHistorySDO(
    override var localId: String,
    override var companyId: Long? = null,
    override var active: Boolean = true,
    var productId: String? = null,
    var quantity: Int = 0,
    var dateRealization: LocalDateTime? = null,
    var datePrevision: LocalDateTime? = null,
    var operationType: EnumOperationType? = null,
    var description: String? = null,
    var userId: String? = null
): BaseSDO()