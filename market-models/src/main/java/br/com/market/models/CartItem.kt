package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "carts_items",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
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
    indices = [Index(value = ["company_id"]), Index(value = ["product_id"]), Index(value = ["purchase_cart_id"])]
)
data class CartItem(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var quantity: Int = 0,
    @ColumnInfo("product_id")
    var productId: UUID? = null,
    @ColumnInfo("purchase_cart_id")
    var purchaseCartId: UUID? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: UUID? = null
): CompanyModel()