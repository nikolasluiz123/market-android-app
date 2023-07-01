package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumUnit
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryBrand::class,
            parentColumns = ["id"],
            childColumns = ["category_brand_id"]
        ),
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["category_brand_id"]), Index(value = ["company_id"])]
)
data class Product(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var price: Double = 0.0,
    var quantity: Double = 0.0,
    @ColumnInfo(name = "quantity_unit")
    var quantityUnit: EnumUnit? = null,
    @ColumnInfo(name = "category_brand_id")
    var categoryBrandId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: Long? = null
) : CompanyModel()