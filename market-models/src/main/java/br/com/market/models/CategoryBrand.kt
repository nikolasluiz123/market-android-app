package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
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
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["category_id"]), Index(value = ["brand_id"]), Index(value = ["company_id"])]
)
data class CategoryBrand(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "category_id")
    var categoryId: UUID? = null,
    @ColumnInfo(name = "brand_id")
    var brandId: UUID? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: UUID? = null
) : CompanyModel()