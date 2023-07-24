package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumPaymentType
import br.com.market.models.base.MarketModel
import java.time.LocalDateTime
import java.util.*

@Entity(
    tableName = "purchases_carts",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        ),
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["client_id"]
        )
    ],
    indices = [Index(value = ["market_id"]), Index(value = ["client_id"])]
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
    @ColumnInfo("market_id")
    override var marketId: Long? = null
): MarketModel()