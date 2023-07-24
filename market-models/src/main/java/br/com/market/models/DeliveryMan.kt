package br.com.market.models

import androidx.room.*
import br.com.market.models.base.MarketModel
import java.util.*

@Entity(
    tableName = "delivery_man",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        ),
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicle_id"]
        )
    ],
    indices = [Index(value = ["market_id"]), Index(value = ["user_id"]), Index(value = ["vehicle_id"])]
)
data class DeliveryMan(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    @ColumnInfo("user_id")
    var userId: String? = null,
    @ColumnInfo("vehicle_id")
    var vehicleId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
): MarketModel()