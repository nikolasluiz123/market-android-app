package br.com.market.localdataaccess.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import br.com.market.core.filter.base.BaseSearchFilter
import br.com.market.domain.CategoryDomain
import br.com.market.models.Category
import java.util.*

/**
 * Classe para manipulação dos dados da categoria na base local
 *
 * @author Nikolas Luiz Schmitt
 */
@Dao
abstract class CategoryDAO : AbstractBaseDAO() {

    /**
     * Função que busca as categorias de forma paginada
     *
     * @param limit Linha na tabela onde a consulta inicia
     * @param offset Linha na tabela onde a consulta termina
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findCategories(filter: BaseSearchFilter): PagingSource<Int, CategoryDomain> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        select.add(" select c.* ")

        val from = StringJoiner("\r\n")
        from.add(" from categories c ")

        val where = StringJoiner("\r\n")
        where.add(" where c.active ")

        if (!filter.quickFilter.isNullOrBlank()) {
            where.add(" and c.name like ? ")
            params.add("%${filter.quickFilter}%")
        }

        val orderBy = StringJoiner("\r\n")
        orderBy.add(" order by c.name ")

        val sql = StringJoiner("\r\n")
        sql.add(select.toString())
        sql.add(from.toString())
        sql.add(where.toString())
        sql.add(orderBy.toString())

        return executeQueryFindCategories(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    @RawQuery(observedEntities = [Category::class])
    abstract fun executeQueryFindCategories(query: SupportSQLiteQuery): PagingSource<Int, CategoryDomain>

    fun findCategoriesLov(filter: BaseSearchFilter): PagingSource<Int, CategoryDomain> {
        val params = mutableListOf<Any>()

        val select = StringJoiner("\r\n")
        select.add(" select c.* ")

        val from = StringJoiner("\r\n")
        from.add(" from categories c ")

        val where = StringJoiner("\r\n")
        where.add(" where c.active ")

        if (!filter.quickFilter.isNullOrBlank()) {
            where.add(" and c.name like ? ")
            params.add("%${filter.quickFilter}%")
        }

        val orderBy = StringJoiner("\r\n")
        orderBy.add(" order by c.name ")

        val sql = StringJoiner("\r\n")
        sql.add(select.toString())
        sql.add(from.toString())
        sql.add(where.toString())
        sql.add(orderBy.toString())

        return executeQueryFindCategoriesLov(SimpleSQLiteQuery(sql.toString(), params.toTypedArray()))
    }

    @RawQuery(observedEntities = [Category::class])
    abstract fun executeQueryFindCategoriesLov(query: SupportSQLiteQuery): PagingSource<Int, CategoryDomain>

    /**
     * Função para salvar uma categoria
     *
     * @param category Categoria que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(category: Category)

    /**
     * Função para salvar uma ou mais categorias em uma lista
     *
     * @param categories Lista de categorias pra salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(categories: List<Category>)

    /**
     * Função para buscar uma categoria pelo id
     *
     * @param categoryId Id da categoria que deseja buscar
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from categories where id = :categoryId")
    abstract suspend fun findById(categoryId: String): Category

    /**
     * Função para atualizar a flag [active] de uma categoria com o [categoryId] informado
     *
     * @param categoryId Id da categoria
     * @param active Flag que indica se o registro está ativo ou não
     * @param sync Flag que indica se o registro foi sincronizado com a base remota ou não
     *
     * @author Nikolas Luiz Schmitt
     */
    @Transaction
    open suspend fun updateActive(categoryId: String) {
        toggleCategoryActive(categoryId)
        toggleBrandActive(categoryId)
    }

    @Query("update categories set active = not active where id = :categoryId")
    abstract suspend fun toggleCategoryActive(categoryId: String)

    @Query("update categories_brands set active = not active where category_id = :categoryId")
    abstract suspend fun toggleBrandActive(categoryId: String)

    /**
     * Função que busca todas as categorias que não foram sincronizadas
     * com a base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from categories where synchronized = 0")
    abstract suspend fun findCategoriesNotSynchronized(): List<Category>

    @Query("delete from categories")
    abstract suspend fun clearAll()
}