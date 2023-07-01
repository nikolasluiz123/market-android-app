package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumOperationType
import br.com.market.models.base.CompanyModel
import java.time.LocalDateTime
import java.util.*

@Entity(
    tableName = "storage_operations_history",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        ),
        ForeignKey(
            entity = Company::class,
            parentColumns = ["id"],
            childColumns = ["company_id"]
        )
    ],
    indices = [Index(value = ["product_id"]), Index(value = ["company_id"]), Index(value = ["user_id"])]
)
data class StorageOperationHistory(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    @ColumnInfo("date_realization")
    var dateRealization: LocalDateTime? = null,
    @ColumnInfo("date_prevision")
    var datePrevision: LocalDateTime? = null,
    @ColumnInfo("operation_type")
    var operationType: EnumOperationType? = null,
    var description: String? = null,
    @ColumnInfo("user_id")
    var userId: String? = null,
    @ColumnInfo("product_id")
    var productId: String? = null,
    var quantity: Int? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: Long? = null
) : CompanyModel()