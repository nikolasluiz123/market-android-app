package br.com.market.localdataaccess.dao

import androidx.room.*
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
abstract class BrandDAO {

    /**
     * Função que busca as marcas de forma paginada
     *
     * @param position Linha na tabela onde a consulta inicia
     * @param loadSize Linha na tabela onde a consulta termina
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query(
        " select b.* " +
                " from brands b " +
                " inner join categories_brands cb on b.id = cb.brand_id " +
                " where cb.category_id = :categoryId " +
                " limit :loadSize offset :position "
    )
    abstract suspend fun findBrands(categoryId: UUID, position: Int, loadSize: Int): List<BrandDomain>

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
    abstract suspend fun findById(brandId: UUID): Brand

    /**
     * Função para atualizar a flag [active] de uma marca com o [brandId] informado
     *
     * @param brandId Id da marca
     * @param active Flag que indica se o registro está ativo ou não
     * @param sync Flag que indica se o registro foi sincronizado com a base remota ou não
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update brands set active = :active, synchronized = :sync where id = :brandId ")
    abstract suspend fun updateActive(brandId: UUID, active: Boolean, sync: Boolean)

    /**
     * Função que facilita a mudança de ativo e inativo.
     *
     * @see updateActive
     *
     * @param brand Marca que deseja reativar ou inativar.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(brand: Brand) {
        updateActive(brand.id, !brand.active, brand.synchronized)
    }

    /**
     * Função que busca todas as marcas que não foram sincronizadas
     * com a base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from brands where synchronized = false")
    abstract suspend fun findBrandsNotSynchronized(): List<Brand>

    /**
     * Função que busca todas as categoria marca que não foram sincronizadas
     * com a base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from categories_brands where synchronized = false")
    abstract suspend fun findCategoryBrandsNotSynchronized(): List<CategoryBrand>
}