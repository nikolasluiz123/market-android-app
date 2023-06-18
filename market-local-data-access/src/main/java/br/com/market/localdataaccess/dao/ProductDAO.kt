package br.com.market.localdataaccess.dao

import androidx.room.*
import br.com.market.localdataaccess.tuples.ProductImageTuple
import br.com.market.models.Product
import java.util.*

@Dao
abstract class ProductDAO : AbstractBaseDAO() {

    @Query("select * from products where id = :productId and active")
    abstract suspend fun findProductById(productId: String): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(products: List<Product>)

    @Query(" select p.id as productId, " +
            "         p.name as productName, " +
            "         p.price as productPrice, " +
            "         p.quantity as productQuantity, " +
            "         p.quantity_unit as productQuantityUnit, " +
            "         p.category_brand_id as categoryBrandId, " +
            "         p.active as productActive, " +
            "         pi.bytes as imageBytes, " +
            "         pi.imageUrl as imageUrl " +
            " from products p " +
            " inner join products_images pi on pi.id = (select id from products_images where product_id = p.id and principal and active) " +
            " inner join categories_brands cb on p.category_brand_id = cb.id " +
            " where cb.category_id = :categoryId and cb.brand_id = :brandId and p.active " +
            " limit :limit offset :offset ")
    abstract suspend fun findProducts(categoryId: String, brandId: String, limit: Int, offset: Int): List<ProductImageTuple>

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