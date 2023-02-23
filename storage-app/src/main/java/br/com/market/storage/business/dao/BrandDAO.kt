package br.com.market.storage.business.dao

import androidx.room.*
import br.com.market.storage.business.dao.tuples.BrandTuple
import br.com.market.storage.business.exceptions.InvalidStorageOperationException
import br.com.market.storage.business.exceptions.NoResultException
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.models.ProductBrand
import br.com.market.storage.ui.domains.ProductBrandDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

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
                "where p.id = :productId "
    )
    abstract fun finProductBrandsByProductId(productId: Long): Flow<List<ProductBrandDomain>>

    @Query(
        "select b.id as brandId, " +
                "p.name | ' ' | b.name as completeProductName, " +
                "pb.count as count " +
                "from products p " +
                "inner join products_brands pb on p.id = pb.product_id " +
                "inner join brands b on b.id = pb.brand_id " +
                "where p.id = :productId "
    )
    abstract fun findProductBrands(productId: Long): Flow<List<BrandTuple>>

    @Query("select * from brands where sincronized = 0")
    abstract fun findAllBrandsNotSincronized(): Flow<List<Brand>>

    @Delete
    abstract suspend fun deleteBrand(brand: Brand)

    @Query("select * from products_brands pb where pb.product_id = :id")
    abstract fun findByProductId(id: Long): Flow<List<ProductBrand>>

    suspend fun sumStorageCount(productId: Long, brandId: Long, count: Int) {
        if (notHaveDataForUpdate(productId, brandId)) {
            throw NoResultException("Não há registro de estoque para o Produto e/ou Marca especificado(s)")
        }

        sumCount(count, productId, brandId)
    }

    suspend fun subtractStorageCount(productId: Long, brandId: Long, count: Int) {
        if (notHaveDataForUpdate(productId, brandId)) {
            throw NoResultException("Não há registro de estoque para o Produto e/ou Marca especificado(s)")
        }

        if (newStorageCountIsInvalid(count, productId, brandId)) {
            throw InvalidStorageOperationException("A quantidade a ser reduzida é maior do que a quantidade em estoque.")
        }

        subtractCount(count, productId, brandId)
    }

    @Query("select count < :count from products_brands where product_id = :productId and brand_id = :brandId")
    abstract fun newStorageCountIsInvalid(count: Int, productId: Long, brandId: Long): Boolean

    @Query(
        "select (" +
                "not exists(select pb.id from products_brands pb where pb.product_id = :productId and pb.brand_id = :brandId)" +
                ")"
    )
    abstract fun notHaveDataForUpdate(productId: Long, brandId: Long): Boolean

    @Query("UPDATE products_brands set count = count + :count where product_id = :productId and brand_id = :brandId")
    abstract suspend fun sumCount(count: Int, productId: Long, brandId: Long)

    @Query("UPDATE products_brands set count = count - :count where product_id = :productId and brand_id = :brandId")
    abstract suspend fun subtractCount(count: Int, productId: Long, brandId: Long)

}