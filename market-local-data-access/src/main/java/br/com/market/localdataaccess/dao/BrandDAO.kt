package br.com.market.localdataaccess.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import br.com.market.core.filter.BrandFilter
import br.com.market.core.filter.base.BaseSearchFilter
import br.com.market.domain.BrandDomain
import br.com.market.models.Brand
import br.com.market.models.CategoryBrand
import java.util.*

/**
 * Classe para manipulação dos dados da marca na base local
 *
 * @author Nikolas Luiz Schmitt
 */
@Dao
abstract class BrandDAO : AbstractBaseDAO() {

    /**
     * Função que busca as marcas de forma paginada
     *
     * @param limit Linha na tabela onde a consulta termina
     * @param offset Linha na tabela onde a consulta inicia
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findBrands(filter: BrandFilter): PagingSource<Int, BrandDomain> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        select.add(" select b.* ")

        val from = StringJoiner("\r\n")
        from.add(" from brands b ")

        val where = StringJoiner("\r\n")
        where.add(" where b.active ")

        if (filter.categoryId != null) {
            from.add(" inner join categories_brands cb on b.id = cb.brand_id and cb.active ")
            where.add(" and cb.category_id = ? ")

            params.add(filter.categoryId!!)
        }

        if (!filter.quickFilter.isNullOrBlank()) {
            where.add(" and b.name like ? ")
            params.add("%${filter.quickFilter}%")
        }

        val orderBy = StringJoiner("\r\n")
        orderBy.add(" order by b.name ")

        val sql = StringJoiner("\r\n")
        sql.add(select.toString())
        sql.add(from.toString())
        sql.add(where.toString())
        sql.add(orderBy.toString())

        return executeQueryFindBrands(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    fun findBrandsLov(filter: BaseSearchFilter): PagingSource<Int, BrandDomain> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        select.add(" select b.* ")

        val from = StringJoiner("\r\n")
        from.add(" from brands b ")

        val where = StringJoiner("\r\n")
        where.add(" where b.active ")

        if (!filter.quickFilter.isNullOrBlank()) {
            where.add(" and p.name like ? ")
            params.add("%${filter.quickFilter}%")
        }

        val orderBy = StringJoiner("\r\n")
        orderBy.add(" order by b.name ")

        val sql = StringJoiner("\r\n")
        sql.add(select.toString())
        sql.add(from.toString())
        sql.add(where.toString())
        sql.add(orderBy.toString())

        return executeQueryFindBrandsLov(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    @RawQuery(observedEntities = [Brand::class])
    abstract fun executeQueryFindBrands(query: SupportSQLiteQuery): PagingSource<Int, BrandDomain>

    @RawQuery(observedEntities = [Brand::class])
    abstract fun executeQueryFindBrandsLov(query: SupportSQLiteQuery): PagingSource<Int, BrandDomain>

    /**
     * Função para salvar uma marca
     *
     * @param brand Marca que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveBrand(brand: Brand)

    /**
     * Função para salvar a entidade intermediária [CategoryBrand]
     *
     * @param categoryBrand
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveCategoryBrand(categoryBrand: CategoryBrand)

    /**
     * Função para salvar uma lista de [CategoryBrand]
     *
     * @param categoryBrands
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveCategoryBrands(categoryBrands: List<CategoryBrand>)

    /**
     * Função para salvar [Brand] e [CategoryBrand] uma após
     * a outra em uma transação.
     *
     * @param brand
     * @param categoryBrand
     *
     * @author Nikolas Luiz Schmitt
     */
    @Transaction
    open suspend fun saveBrandAndReference(brand: Brand, categoryBrand: CategoryBrand) {
        saveBrand(brand)
        saveCategoryBrand(categoryBrand)
    }

    /**
     * Função para salvar uma ou mais marcas em uma lista
     *
     * @param brands Lista de marcas pra salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveBrands(brands: List<Brand>)

    /**
     * Função para salvar uma lista de [Brand] e [CategoryBrand]
     * uma após a outra em uma transação.
     *
     * @param brands
     * @param categoryBrands
     *
     * @author Nikolas Luiz Schmitt
     */
    @Transaction
    open suspend fun saveBrandsAndReferences(brands: List<Brand>, categoryBrands: List<CategoryBrand>) {
        saveBrands(brands)
        saveCategoryBrands(categoryBrands)
    }

    /**
     * Função para buscar uma marca pelo id
     *
     * @param brandId Id da marca que deseja buscar
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from brands where id = :brandId")
    abstract suspend fun findBrandById(brandId: String): Brand

    @Query("select * from categories_brands where brand_id = :brandId and category_id = :categoryId ")
    abstract suspend fun findCategoryBrandBy(brandId: String, categoryId: String): CategoryBrand?

    /**
     * Função para atualizar a flag [active] de uma marca com o [brandId] informado
     *
     * @param brandId Id da marca
     * @param active Flag que indica se o registro está ativo ou não
     * @param sync Flag que indica se o registro foi sincronizado com a base remota ou não
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update categories_brands set active = :active, synchronized = :sync where brand_id = :brandId and category_id = :categoryId ")
    abstract suspend fun updateActive(brandId: String, categoryId: String, active: Boolean, sync: Boolean)

    /**
     * Função que facilita a mudança de ativo e inativo.
     *
     * @see updateActive
     *
     * @param categoryBrand Marca que deseja reativar ou inativar.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(categoryBrand: CategoryBrand) {
        updateActive(
            brandId = categoryBrand.brandId!!,
            categoryId = categoryBrand.categoryId!!,
            active = !categoryBrand.active,
            sync = categoryBrand.synchronized
        )
    }

    /**
     * Função que busca todas as marcas que não foram sincronizadas
     * com a base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from brands where synchronized = 0")
    abstract suspend fun findBrandsNotSynchronized(): List<Brand>

    /**
     * Função que busca todas as categoria marca que não foram sincronizadas
     * com a base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from categories_brands where synchronized = 0")
    abstract suspend fun findCategoryBrandsNotSynchronized(): List<CategoryBrand>

    @Query("delete from brands")
    abstract suspend fun clearAllBrands()

    @Query("delete from categories_brands")
    abstract suspend fun clearAllCategoryBrands()

    @Transaction
    open suspend fun clearAll() {
        clearAllCategoryBrands()
        clearAllBrands()
    }
}