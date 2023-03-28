package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.sdo.product.DeleteProductSDO
import br.com.market.servicedataaccess.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.services.ProductService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


/**
 * Classe usada para realizar operações dos end points do produto.
 *
 * @author Nikolas Luiz Schmitt
 *
 * @property context Contexto de uso diversificado
 * @property productService Interface para acesso do serviço.
 *
 */
class ProductWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productService: ProductService
) : BaseWebClient(context) {

    /**
     * Função para salvar um produto na base de dados remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param product2 Produto que deseja salvar na base remota.
     *
     */
//    suspend fun saveProduct(product2: Product2): PersistenceResponse {
//        return persistenceServiceErrorHandlingBlock(
//            codeBlock = {
//                val newProductSDO = NewProductSDO(localProductId = product2.id, name = product2.name, imageUrl = product2.imageUrl)
//                productService.saveProduct(getToken(), newProductSDO).getPersistenceResponseBody()
//            }
//        )
//    }

    /**
     * Função para alterar um produto na base de dados remota.
     *
     * @param product2 Produto que deseja alterar na base remota.
     *
     * @author Nikolas Luiz Schmitt
     */
//    suspend fun updateProduct(product2: Product2): PersistenceResponse {
//        return persistenceServiceErrorHandlingBlock(
//            codeBlock = {
//                val updateProductSDO = UpdateProductSDO(localProductId = product2.id, name = product2.name, imageUrl = product2.imageUrl)
//                productService.updateProduct(getToken(), updateProductSDO).getPersistenceResponseBody()
//            }
//        )
//    }

    /**
     * Função para deletar o Produto e todos os registros que possuem referência.
     *
     * @param productLocalId Id do produto gerado na base local.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun deleteProduct(productLocalId: UUID): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                productService.deleteProduct(getToken(), DeleteProductSDO(productLocalId)).getResponseBody()
            }
        )
    }

    /**
     * Função responsável par deletar 'N' produtos da base remota, utilizada no sincronismo
     * das bases.
     *
     * @param product2s Produtos que serão deletados na base remota
     *
     * @author Nikolas Luiz Schmitt
     */
//    suspend fun deleteProducts(product2s: List<Product2>): MarketServiceResponse {
//        val productSDOs = product2s.map { DeleteProductSDO(it.id) }
//
//        return serviceErrorHandlingBlock(
//            codeBlock = {
//                productService.deleteProducts(getToken(), productSDOs).getResponseBody()
//            }
//        )
//    }

    /**
     * Função que sincroniza as informações dos produtos, enviando quem foi criado e quem foi alterado
     * para a base remota.
     *
     * @author Nikolas Luiz Schmitt
     *
     * @param product2s Produtos que deseja sincronizar.
     *
     */
//    suspend fun syncProducts(product2s: List<Product2>): MarketServiceResponse {
//        return serviceErrorHandlingBlock(
//            codeBlock = {
//                val dtoList = product2s.map { NewProductSDO(localProductId = it.id, name = it.name, imageUrl = it.imageUrl) }
//                productService.syncProducts(getToken(), dtoList).getResponseBody()
//            }
//        )
//    }

    /**
     * Função para buscar todos os Product do serviço.
     *
     * @author Nikolas Luiz Schmitt
     */
//    suspend fun findAllProducts(): ReadResponse<Product2> {
//        return readServiceErrorHandlingBlock(
//            codeBlock = {
//                val readResponse = productService.findAllProducts(getToken()).getReadResponseBody()
//                val values = readResponse.values.map { Product2(id = it.localProductId, name = it.name, imageUrl = it.imageUrl, synchronized = true) }
//                ReadResponse(values, readResponse.code, readResponse.success, readResponse.error)
//            }
//        )
//    }

}