package br.com.market.models

import androidx.room.*
import br.com.market.models.base.MarketModel
import java.util.*

@Entity(
    tableName = "categories_brands",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"]
        ),
        ForeignKey(
            entity = Brand::class,
            parentColumns = ["id"],
            childColumns = ["brand_id"]
        ),
        ForeignKey(
            entity = Market::class,
            parentColumns = ["id"],
            childColumns = ["market_id"]
        )
    ],
    indices = [Index(value = ["category_id"]), Index(value = ["brand_id"]), Index(value = ["market_id"])]
)
data class CategoryBrand(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "category_id")
    var categoryId: String? = null,
    @ColumnInfo(name = "brand_id")
    var brandId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("market_id")
    override var marketId: Long? = null
) : MarketModel()