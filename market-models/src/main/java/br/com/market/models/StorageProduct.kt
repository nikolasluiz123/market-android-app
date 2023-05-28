package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.util.*

@Entity(
    tableName = "storage_products",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["product_id"]), Index(value = ["company_id"])]
)
data class StorageProduct(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var quantity: Int = 0,
    @ColumnInfo(name = "product_id")
    var productId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: String? = null
): CompanyModel()