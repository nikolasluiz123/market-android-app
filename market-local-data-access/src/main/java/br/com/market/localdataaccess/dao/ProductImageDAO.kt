package br.com.market.localdataaccess.dao

import androidx.room.*
import br.com.market.models.ProductImage
import java.util.*

@Dao
abstract class ProductImageDAO : AbstractBaseDAO() {

    @Query("select * from products_images where product_id = :productId and active")
    abstract suspend fun findProductImagesBy(productId: String): List<ProductImage>

    @Query("select * from products_images where id = :id and active")
    abstract suspend fun findProductImageBy(id: String?): ProductImage?

    @Transaction
    open suspend fun updateImage(image: ProductImage) {
        if (image.principal) {
            updateFlagPrincipalProductImages(productId = image.productId!!, productImageId = image.id)
        }

        save(listOf(image))
    }
    @Query("update products_images set principal = 0 " +
            "where product_id = :productId " +
            "and id != :productImageId " +
            "and active ")
    abstract suspend fun updateFlagPrincipalProductImages(productId: String, productImageId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(images: List<ProductImage>)

    @Query("update products_images set active = not active, principal = 0, synchronized = :sync where id = :id")
    abstract suspend fun toggleActive(id: String, sync: Boolean)

    @Query("select * from products_images where synchronized = 0")
    abstract suspend fun findProductImagesNotSynchronized(): List<ProductImage>

}