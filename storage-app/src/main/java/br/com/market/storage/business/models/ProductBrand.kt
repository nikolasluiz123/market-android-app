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
    var id: Long? = null,
    @ColumnInfo(name = "product_id")
    var productId: Long? = null,
    @ColumnInfo(name = "brand_id")
    var brandId: Long? = null,
    var count: Int = 0,
    override var synchronized: Boolean = false,
    override var active: Boolean = true
): BaseModel()