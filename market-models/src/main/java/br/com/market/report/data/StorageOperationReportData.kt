package br.com.market.report.data

import br.com.market.enums.EnumOperationType
import java.time.LocalDateTime

class StorageOperationReportData(
    val datePrevision: LocalDateTime?,
    val dateRealization: LocalDateTime,
    val operationType: EnumOperationType,
    val quantity: Int
)