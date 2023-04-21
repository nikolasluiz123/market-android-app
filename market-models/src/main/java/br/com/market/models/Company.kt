package br.com.market.models

import androidx.room.*
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
    var id: Long? = null,
    var name: String,
    @ColumnInfo("theme_definitions_id")
    var themeDefinitionsId: UUID? = null,
)