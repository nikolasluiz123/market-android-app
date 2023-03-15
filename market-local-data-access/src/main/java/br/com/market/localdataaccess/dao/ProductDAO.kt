package br.com.market.localdataaccess.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import br.com.market.models.Product
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Classe para realizar operações referentes a entidade de Produto
 * no banco local do dispositivo usando Room.
 *
 * @author Nikolas Luiz Schmitt
 */
@Dao
abstract class ProductDAO {

    /**
     * Função para salvar o Produto na base local.
     *
     * Ao ocorrer um conflito, ou seja, dois IDs iguais,
     * será realizada a substituição de uma entidade pela outra
     *
     * @param product Produto que será salvo.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Insert(onConflict = REPLACE)
    abstract suspend fun saveProduct(product: Product)

    /**
     * Função para buscar todos os proutos ativos da base local.
     *
     * O retorno é um Flow para que seja possível ficar observando
     * e realizar operações quando a tabela for atualizada, dessa forma
     * conseguimos refletir essa atualização em tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from products where active = true")
    abstract fun findActiveAllProducts(): Flow<List<Product>>

    @Query("select * from products where active = false and synchronized = false")
    abstract suspend fun findAllInactiveAndNotSynchronizedProducts(): List<Product>

    /**
     * Função para buscar um produto com um id específico.
     *
     * O retorno é um Flow para que a tela de detalhes do produto
     * seja atualizada quando a tabela sofrer alguma mudança.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from products where id = :productId")
    abstract fun findProductById(productId: UUID): Flow<Product?>

    @Query("select b.id " +
            "from brands b " +
            "inner join products_brands pb on pb.brand_id = b.id " +
            "where pb.product_id = :productId ")
    abstract suspend fun findBrandIdsByProductId(productId: UUID): List<UUID>

    /**
     * Função para remover fisicamente o produto e suas referencias.
     *
     * Essa função executa as remoções de registros de 3 tabelas, sendo
     * que essas 3 operações estão dentro de uma transação. Isso faz com
     * que nada seja removido parcialmente.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Transaction
    open suspend fun deleteProductAndReferences(productId: UUID) {
        val brandIds = findBrandIdsByProductId(productId)
        deleteAllProductBrandOfProduct(productId)
        deleteAllBrandsOfProduct(brandIds.toTypedArray())
        deleteProduct(productId)
    }

    /**
     * Função responsável por remover fisicamente todos os registros
     * da tabela ProductBrand que tenha referência com o ID do produto
     * especificado.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("delete from products_brands where product_id = :productId")
    abstract suspend fun deleteAllProductBrandOfProduct(productId: UUID)

    /**
     * Função resposável por remover as Brands do produto especificado.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("delete from brands where id in (:brandIds)")
    abstract suspend fun deleteAllBrandsOfProduct(brandIds: Array<UUID>)

    /**
     * Função responsável por remover o produto com o id especificado.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("delete from products where id = :productId")
    abstract suspend fun deleteProduct(productId: UUID)

    /**
     * Função para inativar o produto e suas referencias.
     *
     * Essa função executa as inativações de registros de 3 tabelas, sendo
     * que essas 3 operações estão dentro de uma transação. Isso faz com
     * que nada seja inativado parcialmente.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Transaction
    open suspend fun inactivateProductAndReferences(productId: UUID) {
        inactivateAllProductBrandOfProduct(productId)
        inactivateAllBrandsOfProduct(productId)
        inactivateProduct(productId)
    }

    /**
     * Função responsável por inativar todos os registros
     * da tabela ProductBrand que tenha referência com o ID do produto
     * especificado.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update products_brands set active = false, synchronized = false where product_id = :productId")
    abstract suspend fun inactivateAllProductBrandOfProduct(productId: UUID)

    /**
     * Função resposável por inativar as Brands do produto especificado.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update brands set active = false, synchronized = false where id in " +
            "(select pb.brand_id " +
            "from products p " +
            "inner join products_brands pb on pb.product_id = p.id " +
            "where p.id = :productId " +
            ")")
    abstract suspend fun inactivateAllBrandsOfProduct(productId: UUID)

    /**
     * Função responsável por inativar o produto com o id especificado.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("update products set active = false, synchronized = false where id = :productId")
    abstract suspend fun inactivateProduct(productId: UUID)

    /**
     * Função responsável por retornar quantos registros não estão sincronizados
     * com a base de dados remota. É dessa forma que damos o feedback para o usuário
     * que ele precisa enviar as informações que estão presentes apenas em seu dispositivo.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select (select count(*) from products where synchronized = false) + " +
            "(select count(*) from brands where synchronized = false) + " +
            "(select count(*) from products_brands where synchronized = false)")
    abstract fun findCountOfNotSynchronizedRegisters(): Flow<Long>

    /**
     * Função responsável por retornar todos os produtos que precisam ser
     * sincronizados.
     *
     * @author Nikolas Luiz Schmitt
     */
    @Query("select * from products where synchronized = false and active = true")
    abstract suspend fun findAllActiveProductsNotSynchronized(): List<Product>

}