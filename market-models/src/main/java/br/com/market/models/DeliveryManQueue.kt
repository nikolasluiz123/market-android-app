package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "delivery_man_queue",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        ),
        ForeignKey(
            entity = PurchaseCart::class,
            parentColumns = ["id"],
            childColumns = ["purchase_cart_id"]
        ),
        ForeignKey(
            entity = DeliveryMan::class,
            parentColumns = ["id"],
            childColumns = ["delivery_man_id"]
        )
    ],
    indices = [Index(value = ["company_id"]), Index(value = ["purchase_cart_id"]), Index(value = ["delivery_man_id"])]
)
data class DeliveryManQueue(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    @ColumnInfo("delivery_started")
    var deliveryStarted: Boolean = false,
    @ColumnInfo("purchase_cart_id")
    var purchaseCartId: String? = null,
    @ColumnInfo("delivery_man_id")
    var deliveryManId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: Long? = null
): CompanyModel()