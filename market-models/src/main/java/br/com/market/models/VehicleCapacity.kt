package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumUnit
import br.com.market.models.base.MarketModel
import java.util.*

@Entity(
    tableName = "vehicle_capacity",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        ),
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicle_id"]
        )
    ],
    indices = [Index(value = ["market_id"]), Index(value = ["vehicle_id"])]
)
data class VehicleCapacity(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var capacity: Int = 0,
    @ColumnInfo(name = "capacity_unit")
    var capacityUnit: EnumUnit? = null,
    @ColumnInfo("vehicle_id")
    var vehicleId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
): MarketModel()