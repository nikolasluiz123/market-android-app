package br.com.market.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.market.models.base.BaseModel
import java.util.*

@Entity(tableName = "theme_definitions")
data class ThemeDefinitions(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    @ColumnInfo("color_primary")
    var colorPrimary: String? = null,
    @ColumnInfo("color_secondary")
    var colorSecondary: String? = null,
    @ColumnInfo("color_tertiary")
    var colorTertiary: String? = null,
    @ColumnInfo("image_logo")
    var imageLogo: ByteArray? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true
) : BaseModel() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThemeDefinitions

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}