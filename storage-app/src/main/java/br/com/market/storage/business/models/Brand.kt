package br.com.market.storage.business.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brands")
data class Brand(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "",
    override var synchronized: Boolean = false,
    override var active: Boolean = true
): BaseModel()
