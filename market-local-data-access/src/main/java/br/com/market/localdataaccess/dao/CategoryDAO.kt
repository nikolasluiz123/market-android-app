package br.com.market.localdataaccess.dao

import androidx.room.*
import br.com.market.domain.CategoryDomain
import br.com.market.models.Category
import java.util.*

/**
 * Classe para manipulação dos dados da categoria na base local
 *
 * @author Nikolas Luiz Schmitt
 */
@Dao
abstract class CategoryDAO {

    /**
     * Função que busca as categorias de forma paginada
     *
     * @param limit Linha na tabela onde a consulta inicia
     * @param offset Linha na tabela onde a consulta termina
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from categories limit :limit offset :offset")
    abstract suspend fun findCategories(limit: Int, offset: Int): List<CategoryDomain>

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
    abstract suspend fun findById(categoryId: UUID): Category

    /**
     * Função para atualizar a flag [active] de uma categoria com o [categoryId] informado
     *
     * @param categoryId Id da categoria
     * @param active Flag que indica se o registro está ativo ou não
     * @param sync Flag que indica se o registro foi sincronizado com a base remota ou não
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update categories set active = :active, synchronized = :sync where id = :categoryId ")
    abstract suspend fun updateActive(categoryId: UUID, active: Boolean, sync: Boolean)

    /**
     * Função que facilita a mudança de ativo e inativo.
     *
     * @see updateActive
     *
     * @param category Categoria que deseja reativar ou inativar.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(category: Category) {
        updateActive(category.id, !category.active, category.synchronized)
    }

    /**
     * Função que busca todas as categorias que não foram sincronizadas
     * com a base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from categories where synchronized = false")
    abstract suspend fun findCategoriesNotSynchronized(): List<Category>
}