package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.util.*

/**
 * Uma classe que representa uma Marca na base local.
 *
 * @property name Nome da Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
@Entity(
    tableName = "brands",
    foreignKeys = [
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["company_id"])]
)
data class Brand(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var name: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: String? = null
) : CompanyModel()
