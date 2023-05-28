package br.com.market.models

import androidx.room.*
import br.com.market.models.base.CompanyModel
import java.time.LocalDateTime
import java.util.*

@Entity(
    tableName = "products_ratings",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["client_id"]
        ),
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["product_id"]), Index(value = ["client_id"]), Index(value = ["company_id"])]
)
data class ProductRating(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var rating: Double = 0.0,
    var comment: String? = null,
    var date: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo("product_id")
    var productId: String? = null,
    @ColumnInfo("client_id")
    var clientId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: String? = null
): CompanyModel()