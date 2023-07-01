package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["company_id"])]
)
data class User(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var token: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: Long? = null
) : CompanyModel()