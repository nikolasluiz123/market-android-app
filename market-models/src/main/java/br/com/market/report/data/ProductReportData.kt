package br.com.market.report.data

import br.com.market.enums.EnumUnit

class ProductReportData(
    val productId: String,
    val productName: String,
    val productQuantity: Double,
    val quantityUnit: EnumUnit,
    val image: ByteArray
)