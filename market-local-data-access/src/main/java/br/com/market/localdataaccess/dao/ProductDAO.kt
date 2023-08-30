package br.com.market.localdataaccess.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import br.com.market.localdataaccess.tuples.ProductImageTuple
import br.com.market.models.Product
import br.com.market.models.StorageOperationHistory
import br.com.market.models.User
import java.util.*

@Dao
abstract class ProductDAO : AbstractBaseDAO() {

    @Query("select * from products where id = :productId and active")
    abstract suspend fun findProductById(productId: String): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(products: List<Product>)

    suspend fun findProducts(
        categoryId: String,
        brandId: String,
        limit: Int,
        offset: Int,
        simpleFilter: String?
    ): List<ProductImageTuple> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        with(select) {
            add(" select p.id as productId, ")
            add("        p.name as productName, ")
            add("        p.price as productPrice, ")
            add("        p.quantity as productQuantity, ")
            add("        p.quantity_unit as productQuantityUnit, ")
            add("        p.category_brand_id as categoryBrandId, ")
            add("        p.active as productActive, ")
            add("        pi.bytes as imageBytes, ")
            add("        pi.imageUrl as imageUrl ")
        }

        val from = StringJoiner("\r\n")
        with(from) {
            add(" from products p ")
            add(" inner join products_images pi on pi.id = (select id from products_images where product_id = p.id and principal and active) ")
            add(" inner join categories_brands cb on p.category_brand_id = cb.id ")
        }

        val where = StringJoiner("\r\n")
        with(where) {
            add(" where cb.category_id = ? and cb.brand_id = ? and p.active ")
            params.add(categoryId)
            params.add(brandId)

            if (!simpleFilter.isNullOrBlank()) {
                add(" and p.name like ? ")
                params.add("%${simpleFilter}%")
            }
        }

        val orderBy = StringJoiner("\r\n")
        with(orderBy) {
            add(" order by p.name ")
            add(" limit ? offset ? ")
        }

        params.add(limit)
        params.add(offset)

        val sql = StringJoiner("\r\n")
        with(sql) {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        return executeQueryFindProducts(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    @RawQuery(observedEntities = [StorageOperationHistory::class, Product::class, User::class])
    abstract suspend fun executeQueryFindProducts(query: SupportSQLiteQuery): List<ProductImageTuple>

    @Transaction
    open suspend fun toggleActiveProductAndImages(productId: String, sync: Boolean) {
        toggleActiveProduct(productId = productId, sync = sync)
        toggleActiveProductImages(productId = productId, sync = sync)
    }

    @Query("update products set active = not active, synchronized = :sync where id = :productId")
    abstract suspend fun toggleActiveProduct(productId: String, sync: Boolean)

    @Query("update products_images set active = not active, principal = 0, synchronized = :sync where product_id = :productId")
    abstract suspend fun toggleActiveProductImages(productId: String, sync: Boolean)

    @Query("select * from products where synchronized = 0")
    abstract suspend fun findProductsNotSynchronized(): List<Product>
}