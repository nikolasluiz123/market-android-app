package br.com.market.storage.business.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.models.ProductBrand
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductDAO {

    @Insert(onConflict = REPLACE)
    abstract suspend fun saveProduct(product: Product)

    @Query("select * from products")
    abstract fun findAllProducts(): Flow<List<Product>>

    @Query("select * from products where active = 0")
    abstract fun findAllProductsInativated(): Flow<List<Product>>

    @Query("select * from products where sincronized = 0")
    abstract fun findAllProductsNotSincronized(): Flow<List<Product>>

    @Query("select * from products_brands where sincronized = 0")
    abstract fun findAllProductsBrandsNotSincronized(): Flow<List<ProductBrand>>

    @Delete
    abstract suspend fun deleteProduct(product: Product)

    suspend fun deleteProductAndReferences(productId: Long) {
        deleteProductBrand(productId)
        deleteBrand(productId)
        deleteProduct(productId)
    }

    @Query("delete from product_brand where product_id = :productId")
    abstract suspend fun deleteProductBrand(productId: Long)

    @Query("delete from brand where id in " +
            "(select array(" +
            "select pb.brand_id " +
            "from product p " +
            "inner join products_brands pb on pb.product_id = p.id " +
            "where p.id = :productId " +
            "))")
    abstract suspend fun deleteBrand(productId: Long)

    @Query("delete from product where id = :productId")
    abstract suspend fun deleteProduct(productId: Long)

    suspend fun inativateProductAndReferences(productId: Long) {
        inativateProductBrand(productId)
        inativateBrand(productId)
        inativateProduct(productId)
    }

    @Query("update product_brand set active = 0 where product_id = :productId")
    abstract suspend fun inativateProductBrand(productId: Long)

    @Query("update brand set active = 0 where id in " +
            "(select array(" +
            "select pb.brand_id " +
            "from product p " +
            "inner join products_brands pb on pb.product_id = p.id " +
            "where p.id = :productId " +
            "))")
    abstract suspend fun inativateBrand(productId: Long)

    @Query("update product set active = 0 where id = :productId")
    abstract suspend fun inativateProduct(productId: Long)

}