package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.time.LocalDate
import java.util.*

@Entity(
    tableName = "card",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["client_id"]
        ),
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["client_id"]), Index(value = ["company_id"])]
)
data class Card(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var accaount: String? = null,
    var agency: String? = null,
    var bank: String? = null,
    var validity: LocalDate? = null,
    @ColumnInfo("client_id")
    var clientId: UUID? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: UUID? = null
) : CompanyModel()