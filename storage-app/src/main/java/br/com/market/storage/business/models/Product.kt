package br.com.market.storage.business.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String = "",
    val imageUrl: String = "",
    override val synchronized: Boolean = false,
    override val active: Boolean = true
) : BaseModel()