package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.localdataaccess.tuples.ProductImageTuple
import br.com.market.models.Product
import java.util.*

@Dao
abstract class ProductDAO {

    @Query("select * from products where id = :productId")
    abstract suspend fun findProductById(productId: UUID): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(product: Product)

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
            " inner join products_images pi on pi.id = (select id from products_images where product_id = p.id limit 1) " +
            " inner join categories_brands cb on p.category_brand_id = cb.id " +
            " where cb.category_id = :categoryId and cb.brand_id = :brandId " +
            " limit :limit offset :offset ")
    abstract suspend fun findProducts(categoryId: UUID, brandId: UUID, limit: Int, offset: Int): List<ProductImageTuple>
}