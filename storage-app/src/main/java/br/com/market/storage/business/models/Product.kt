package br.com.market.storage.business.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "",
    var imageUrl: String = "",
    override var synchronized: Boolean = false,
    override var active: Boolean = true
) : BaseModel()