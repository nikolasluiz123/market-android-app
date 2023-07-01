package br.com.market.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.market.models.base.CompanyModel
import java.util.UUID

@Entity(
    tableName = "devices",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["company_id"])]
)
data class Device(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo(name = "company_id")
    override var companyId: Long? = null,
    var name: String? = null
) : CompanyModel()
