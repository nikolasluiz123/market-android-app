package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import br.com.market.enums.EnumOperationType
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.localdataaccess.tuples.StorageOperationHistoryTuple
import br.com.market.models.Product
import br.com.market.models.StorageOperationHistory
import br.com.market.models.User
import java.util.StringJoiner

@Dao
abstract class StorageOperationsHistoryDAO : AbstractBaseDAO() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(storageOperationHistory: StorageOperationHistory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(storageOperationsHistory: List<StorageOperationHistory>)

    @Query("select * from storage_operations_history where id = :id")
    abstract suspend fun findById(id: String): StorageOperationHistory

    @Query("select * from storage_operations_history where synchronized = 0")
    abstract suspend fun findStorageOperationsHistoryNotSynchronized(): List<StorageOperationHistory>

    suspend fun findStorageOperationsHistory(
        limit: Int,
        offset: Int,
        categoryId: String,
        brandId: String,
        productId: String? = null,
        simpleFilter: String? = null,
        advancedFilter: MovementSearchScreenFilters
    ): List<StorageOperationHistoryTuple> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        with(select) {
            add(" select operation.id as id, ")
            add("        category.id as categoryId, ")
            add("        brand.id as brandId, ")
            add("        product.id as productId, ")
            add("        product.name as productName, ")
            add("        operation.date_realization as dateRealization, ")
            add("        operation.date_prevision as datePrevision, ")
            add("        operation.quantity as quantity, ")
            add("        user.id as responsibleId, ")
            add("        user.name as responsibleName, ")
            add("        operation.description as description, ")
            add("        operation.operation_type as operationType ")
        }

        val from = StringJoiner("\r\n")
        with(from) {
            add(" from storage_operations_history operation ")
            add(" inner join products product on product.id = operation.product_id ")
            add(" inner join categories_brands cb on cb.id = product.category_brand_id ")
            add(" inner join brands brand on brand.id = cb.brand_id ")
            add(" inner join categories category on category.id = cb.category_id ")
            add(" inner join users user on user.id = operation.user_id ")
        }

        val where = StringJoiner("\r\n")
        with(where) {
            add(" where operation.active = 1 ")
            add(" and category.id = ? ")
            add(" and brand.id = ? ")

            params.add(categoryId)
            params.add(brandId)

            if (!productId.isNullOrBlank()) {
                add(" and product.id = ? ")
                params.add(productId)
            }

            if (!simpleFilter.isNullOrBlank()) {
                add(" and ( ")
                add("       product.name like ? or ")
                add("       user.name like ? or ")
                add("       operation.description like ? ")
                add("     ) ")

                params.add("%${simpleFilter}%")
                params.add("%${simpleFilter}%")
                params.add("%${simpleFilter}%")
            }

            if (advancedFilter.productName.isFilterApplied()) {
                add(" and product.name like ? ")
                params.add("%${advancedFilter.productName.value}%")
            }

            if (advancedFilter.description.isFilterApplied()) {
                add(" and operation.description like ? ")
                params.add("%${advancedFilter.description.value}%")
            }

            if (advancedFilter.datePrevision.isFilterApplied()) {
                val dateFrom = advancedFilter.datePrevision.value?.first?.toString()
                val dateTo = advancedFilter.datePrevision.value?.second?.toString()

                when {
                    dateFrom != null && dateTo != null -> {
                        add(" and operation.date_prevision between ? and ? ")
                        params.add(dateFrom)
                        params.add(dateTo)
                    }
                    dateFrom != null -> {
                        add(" and operation.date_prevision >= ? ")
                        params.add(dateFrom)
                    }
                    dateTo != null -> {
                        add(" and operation.date_prevision <= ? ")
                        params.add(dateTo)
                    }
                }
            }

            if (advancedFilter.dateRealization.isFilterApplied()) {
                val dateFrom = advancedFilter.dateRealization.value?.first?.toString()
                val dateTo = advancedFilter.dateRealization.value?.second?.toString()

                when {
                    dateFrom != null && dateTo != null -> {
                        add(" and operation.date_realization between ? and ? ")
                        params.add(dateFrom)
                        params.add(dateTo)
                    }
                    dateFrom != null -> {
                        add(" and operation.date_realization >= ? ")
                        params.add(dateFrom)
                    }
                    dateTo != null -> {
                        add(" and operation.date_realization <= ? ")
                        params.add(dateTo)
                    }
                }
            }

            if (advancedFilter.operationType.isFilterApplied()) {
                add(" and operation.operation_type = ? ")
                params.add(advancedFilter.operationType.value!!)
            }

            if (advancedFilter.quantity.isFilterApplied()) {
                add(" and operation.quantity = ? ")
                params.add(advancedFilter.quantity.value!!)
            }

            if (advancedFilter.responsible.isFilterApplied()) {
                add(" and user.id = ? ")
                params.add(advancedFilter.responsible.value?.second!!)
            }
        }

        val orderBy = StringJoiner("\r\n")
        with(orderBy) {
            add(" order by case operation.operation_type when ? then 1 else 2 end, ")
            add("          operation.date_prevision, ")
            add("          operation.date_realization ")
            add(" limit ? offset ? ")
        }

        params.add(EnumOperationType.ScheduledInput.name)
        params.add(limit)
        params.add(offset)

        val sql = StringJoiner("\r\n")
        with(sql) {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        return executeQueryFindStorageOperationsHistory(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    @RawQuery(observedEntities = [StorageOperationHistory::class, Product::class, User::class])
    abstract suspend fun executeQueryFindStorageOperationsHistory(query: SupportSQLiteQuery): List<StorageOperationHistoryTuple>

    @Query("update storage_operations_history set active = 0, synchronized = :sync where id = :id")
    abstract suspend fun inactivate(id: String, sync: Boolean)

    suspend fun findProductStorageQuantity(productId: String): Int {
        val params = mutableListOf<Any>()

        val sqlSumInputs = StringJoiner("\r\n")
        with(sqlSumInputs) {
            add(" select sum(operation.quantity) ")
            add(" from storage_operations_history operation ")
            add(" where operation.product_id = ? ")
            add(" and operation_type = ? ")

            params.add(productId)
            params.add(EnumOperationType.Input.name)
        }

        val inputs = executeQuerySum(SimpleSQLiteQuery(sqlSumInputs.toString(), params.toTypedArray()))
        params.clear()

        val sqlSumOutputsAndSell = StringJoiner("\r\n")
        with(sqlSumOutputsAndSell) {
            add(" select sum(operation.quantity) ")
            add(" from storage_operations_history operation ")
            add(" where operation.product_id = ? ")
            add(" and (operation_type = ? or operation_type = ?) ")

            params.add(productId)
            params.add(EnumOperationType.Sell.name)
            params.add(EnumOperationType.Output.name)
        }

        val outputsAndSell = executeQuerySum(SimpleSQLiteQuery(sqlSumOutputsAndSell.toString(), params.toTypedArray()))

        return inputs - outputsAndSell
    }

    @RawQuery(observedEntities = [StorageOperationHistory::class])
    abstract suspend fun executeQuerySum(query: SupportSQLiteQuery): Int
}