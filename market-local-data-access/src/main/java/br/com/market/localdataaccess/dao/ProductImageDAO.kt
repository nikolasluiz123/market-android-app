package br.com.market.localdataaccess.dao

import androidx.room.*
import br.com.market.models.ProductImage
import java.util.*

@Dao
abstract class ProductImageDAO {

    @Query("select * from products_images where product_id = :productId")
    abstract suspend fun findProductImagesBy(productId: UUID): List<ProductImage>

    @Transaction
    open suspend fun saveNewImages(images: List<ProductImage>) {
        delete(findProductImagesBy(images[0].productId!!))
        save(images)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(images: List<ProductImage>)

    @Delete
    abstract suspend fun delete(images: List<ProductImage>)
}