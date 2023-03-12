package br.com.market.storage.business.dao

import androidx.room.*
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.ProductBrand
import br.com.market.storage.ui.domains.ProductBrandDomain
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BrandDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveBrand(brand: Brand): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveProductBrand(productBrand: ProductBrand)

    @Query(
        " select b.id as brandId, " +
                "p.name as productName, " +
                "b.name as brandName, " +
                "pb.count as count " +
                "from products p " +
                "inner join products_brands pb on pb.product_id = p.id " +
                "inner join brands b on pb.brand_id = b.id " +
                "where p.id = :productId and b.active = true"
    )
    abstract fun findProductBrandsByProductId(productId: Long?): Flow<List<ProductBrandDomain>>

    @Query("select * from products_brands where brand_id = :brandId")
    abstract suspend fun findProductBrandByBrandId(brandId: Long): ProductBrand

    @Transaction
    open suspend fun inactivateBrandAndReferences(brandId: Long) {
        inactivateProductBrandOfBrand(brandId)
        inactivateBrand(brandId)
    }

    @Query("update products_brands set active = false, synchronized = false where brand_id = :brandId")
    abstract suspend fun inactivateProductBrandOfBrand(brandId: Long)

    @Query("update brands set active = false, synchronized = false where id = :brandId ")
    abstract suspend fun inactivateBrand(brandId: Long)

    @Transaction
    open suspend fun deleteBrandAndReferences(brandId: Long) {
        deleteProductBrandOfBrand(brandId)
        deleteBrand(brandId)
    }

    @Query("delete from products_brands where brand_id = :brandId")
    abstract suspend fun deleteProductBrandOfBrand(brandId: Long)

    @Query("delete from brands where id = :brandId ")
    abstract suspend fun deleteBrand(brandId: Long)

    @Query("select * from brands where active = true and synchronized = false")
    abstract suspend fun findAllActiveBrandsNotSynchronized(): List<Brand>

    @Query("select * from products_brands where active = true and synchronized = false")
    abstract suspend fun findAllActiveProductsBrandsNotSynchronized(): List<ProductBrand>

    @Query("select * from brands where active = false and synchronized = false")
    abstract suspend fun findAllInactiveAndNotSynchronizedBrands(): List<Brand>

}