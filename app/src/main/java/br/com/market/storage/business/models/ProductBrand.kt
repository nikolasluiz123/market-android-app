package br.com.market.storage.business.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "products_brands",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = Brand::class,
            parentColumns = ["id"],
            childColumns = ["brand_id"]
        )
    ]
)
data class ProductBrand(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "brand_id")
    val brandId: Long,
    val count: Int,
    override val sincronized: Boolean = false,
    override val active: Boolean = true
): BaseModel()