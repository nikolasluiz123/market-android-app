package br.com.market.storage.business.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import br.com.market.storage.business.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductDAO {

    @Insert(onConflict = REPLACE)
    abstract suspend fun saveProduct(product: Product): Long

    @Query("select * from products")
    abstract fun findAllProducts(): Flow<List<Product>>

    @Query("select * from products where id = :productId")
    abstract fun findProductById(productId: Long?): Flow<Product?>

    @Transaction
    open suspend fun deleteProductAndReferences(productId: Long) {
        deleteAllProductBrandOfProduct(productId)
        deleteAllBrandsOfProduct(productId)
        deleteProduct(productId)
    }

    @Query("delete from products_brands where product_id = :productId")
    abstract suspend fun deleteAllProductBrandOfProduct(productId: Long)

    @Query("delete from brands where id in " +
            "(select pb.brand_id " +
            "from products p " +
            "inner join products_brands pb on pb.product_id = p.id " +
            "where p.id = :productId " +
            ")")
    abstract suspend fun deleteAllBrandsOfProduct(productId: Long)

    @Query("delete from products where id = :productId")
    abstract suspend fun deleteProduct(productId: Long?)

}