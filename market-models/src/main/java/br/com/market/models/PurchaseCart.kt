package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumPaymentType
import br.com.market.models.base.CompanyModel
import java.time.LocalDateTime
import java.util.*

@Entity(
    tableName = "purchases_carts",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        ),
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["client_id"]
        )
    ],
    indices = [Index(value = ["company_id"]), Index(value = ["client_id"])]
)
data class PurchaseCart(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var date: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo("payment_type")
    var paymentType: EnumPaymentType? = null,
    var delivery: Boolean = false,
    @ColumnInfo("client_id")
    var clientId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: String? = null
): CompanyModel()