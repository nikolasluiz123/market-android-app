package br.com.market.models

import androidx.room.*
import br.com.market.models.base.MarketModel
import java.util.*

@Entity(
    tableName = "products_images",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["product_id"]), Index(value = ["market_id"])]
)
data class ProductImage(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var bytes: ByteArray? = null,
    var imageUrl: String? = null,
    @ColumnInfo(name = "product_id")
    var productId: String? = null,
    @ColumnInfo(defaultValue = "0")
    var principal: Boolean = false,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
) : MarketModel() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImage

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}