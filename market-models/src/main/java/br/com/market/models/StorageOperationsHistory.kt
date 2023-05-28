package br.com.market.models

import androidx.room.*
import br.com.market.enums.EnumOperationType
import br.com.market.models.base.CompanyModel
import java.time.LocalDateTime
import java.util.*

@Entity(
    tableName = "storage_product",
    foreignKeys = [
        ForeignKey(
            entity = StorageProduct::class,
            parentColumns = ["id"],
            childColumns = ["storage_product_id"]
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
    indices = [Index(value = ["storage_product_id"]), Index(value = ["company_id"]), Index(value = ["user_id"])]
)
data class StorageOperationsHistory(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    @ColumnInfo("date_realization")
    var dateRealization: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo("date_prevision")
    var datePrevision: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo("operation_type")
    var operationType: EnumOperationType? = null,
    var description: String? = null,
    @ColumnInfo("storage_product_id")
    var storageProductId: String? = null,
    @ColumnInfo("user_id")
    var userId: String? = null,
    override var synchronized: Boolean = false,
    override var active: Boolean = true,
    @ColumnInfo("company_id")
    override var companyId: String? = null
) : CompanyModel()