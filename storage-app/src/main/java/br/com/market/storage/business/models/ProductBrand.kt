package br.com.market.storage.business.models

import androidx.room.*

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
    ],
    indices = [Index(value = ["product_id"]), Index(value = ["brand_id"], unique = true)]
)
data class ProductBrand(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "brand_id")
    val brandId: Long,
    val count: Int,
    override val sincronized: Boolean = false,
    override val active: Boolean = true
): BaseModel()