package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumUnit
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "vehicle_capacity",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        ),
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicle_id"]
        )
    ],
    indices = [Index(value = ["company_id"]), Index(value = ["vehicle_id"])]
)
data class VehicleCapacity(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var capacity: Int = 0,
    @ColumnInfo(name = "capacity_unit")
    var capacityUnit: EnumUnit? = null,
    @ColumnInfo("vehicle_id")
    var vehicleId: UUID? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: UUID? = null
): CompanyModel()