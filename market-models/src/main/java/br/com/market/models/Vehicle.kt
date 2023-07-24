package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumVehicleType
import br.com.market.models.base.MarketModel
import java.util.*

@Entity(
    tableName = "vehicle",
    foreignKeys = [
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["market_id"])]
)
data class Vehicle(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var type: EnumVehicleType? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
) : MarketModel()