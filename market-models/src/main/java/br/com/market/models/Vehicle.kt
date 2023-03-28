package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumVehicleType
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "vehicle",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["company_id"])]
)
data class Vehicle(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var type: EnumVehicleType? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: UUID? = null
) : CompanyModel()