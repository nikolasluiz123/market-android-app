package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "addresses",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["company_id"])]
)
data class Address(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var state: String? = null,
    var city: String? = null,
    @ColumnInfo("public_place")
    var publicPlace: String? = null,
    var number: String? = null,
    var complement: String? = null,
    var cep: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: UUID? = null
) : CompanyModel()
