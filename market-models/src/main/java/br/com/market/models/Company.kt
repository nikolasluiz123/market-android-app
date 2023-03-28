package br.com.market.models

import androidx.room.*
import br.com.market.models.base.BaseModel
import java.util.*

@Entity(
    tableName = "companies",
    foreignKeys = [
        ForeignKey(
            entity = ThemeDefinitions::class,
            parentColumns = ["id"],
            childColumns = ["theme_definitions_id"]
        )
    ],
    indices = [Index(value = ["theme_definitions_id"])]
)
data class Company(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    var name: String,
    @ColumnInfo("theme_definitions_id")
    var themeDefinitionsId: UUID? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
) : BaseModel()