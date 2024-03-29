package br.com.market.models

import androidx.room.*
import br.com.market.models.base.MarketModel
import java.util.*

@Entity(
    tableName = "carts_items",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = PurchaseCart::class,
            parentColumns = ["id"],
            childColumns = ["purchase_cart_id"]
        )
    ],
    indices = [Index(value = ["market_id"]), Index(value = ["product_id"]), Index(value = ["purchase_cart_id"])]
)
data class CartItem(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var quantity: Int = 0,
    @ColumnInfo("product_id")
    var productId: String? = null,
    @ColumnInfo("purchase_cart_id")
    var purchaseCartId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
): MarketModel()