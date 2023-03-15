package br.com.market.localdataaccess.dao

import androidx.room.*
import br.com.market.domain.ProductBrandDomain
import br.com.market.models.Brand
import br.com.market.models.ProductBrand
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Classe para manipulação dos dados da base local
 *
 * @author Nikolas Luiz Schmitt
 */
@Dao
abstract class BrandDAO {

    /**
     * Função para salvar uma marca, ocorrer um conflito, ou seja, dois IDs iguais,
     * será realizada a substituição de uma entidade pela outra.
     *
     * @param brand Marca que deseja salvar.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveBrand(brand: Brand)

    /**
     * Função para salvar a entidade intermediária ProductBrand
     *
     * @param productBrand Entidade intermediária que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveProductBrand(productBrand: ProductBrand)

    /**
     * Função utilizada para recuperar todas as marcas ativas para exibir na tela.
     *
     * @param productId Id do produto que está sendo editado
     *
     * @author Nikolas Luiz Schmitt
     */
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
    abstract fun findAllActiveProductBrandsByProductId(productId: UUID?): Flow<List<ProductBrandDomain>>

    /**
     * Função para recuperar um ProductBrand pelo ID da Brand.
     *
     * @param brandId Id da Brand
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from products_brands where brand_id = :brandId")
    abstract suspend fun findProductBrandByBrandId(brandId: UUID): ProductBrand

    @Query("select * from brands where id = :brandId")
    abstract suspend fun findByBrandId(brandId: UUID): Brand

    /**
     * Função para inativar uma marca e as suas referências. Essa função
     * está dentro de uma transação para que, caso ocorra um erro, não sejam
     * inativados registros de forma parcial.
     *
     * @param brandId Id da marca que será inativada.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Transaction
    open suspend fun inactivateBrandAndReferences(brandId: UUID) {
        inactivateProductBrandOfBrand(brandId)
        inactivateBrand(brandId)
    }

    /**
     * Função que inativa um ProductBrand que tenha o id da brand especificado.
     *
     * @param brandId Id da Brand
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update products_brands set active = false, synchronized = false where brand_id = :brandId")
    abstract suspend fun inactivateProductBrandOfBrand(brandId: UUID)

    /**
     * Função que inativa a Brand.
     *
     * @param brandId Id da Brand que deseja inativar.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update brands set active = false, synchronized = false where id = :brandId ")
    abstract suspend fun inactivateBrand(brandId: UUID)

    /**
     * Função para excluir fisicamente os registros da brand especificada
     * e suas referências. Essa função está em uma transação para que não
     * haja uma exclusão parcial devido a erros.
     *
     * @param brandId Id da Brand que deseja excluir
     *
     * @author Nikolas Luiz Schmitt
     */
    @Transaction
    open suspend fun deleteBrandAndReferences(brandId: UUID) {
        deleteProductBrandOfBrand(brandId)
        deleteBrand(brandId)
    }

    /**
     * Função para excluir uma ProductBrand pelo id da Brand
     *
     * @param brandId Id da Brand
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("delete from products_brands where brand_id = :brandId")
    abstract suspend fun deleteProductBrandOfBrand(brandId: UUID)

    /**
     * Função para excluir uma Brand de id específico
     *
     * @param brandId Id da Brand
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("delete from brands where id = :brandId ")
    abstract suspend fun deleteBrand(brandId: UUID)

    /**
     * Função para buscar todas as Brands que estão ativas e não foram
     * enviadas para o serviço.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from brands where active = true and synchronized = false")
    abstract suspend fun findAllActiveBrandsNotSynchronized(): List<Brand>

    /**
     * Função para buscar todas as ProductBrand que estão ativas e não foram
     * enviadas para o serviço.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from products_brands where active = true and synchronized = false")
    abstract suspend fun findAllActiveProductsBrandsNotSynchronized(): List<ProductBrand>

    /**
     * Função para buscar todas as Brand que estão inativas e não foram enviadas para o serviço
     * realizar a exclusão.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from brands where active = false and synchronized = false")
    abstract suspend fun findAllInactiveAndNotSynchronizedBrands(): List<Brand>

}