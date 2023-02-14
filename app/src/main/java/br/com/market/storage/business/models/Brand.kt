package br.com.market.storage.business.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brands")
data class Brand(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String = "",
    override val sincronized: Boolean = false,
    override val active: Boolean = true
): BaseModel()
