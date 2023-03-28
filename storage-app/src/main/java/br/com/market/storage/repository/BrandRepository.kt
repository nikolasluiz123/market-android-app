package br.com.market.storage.repository

import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.webclients.BrandWebClient
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject

/**
 * Classe Repository responsável pela manipulação de dados
 * referentes a Brand e ProductBrand.
 *
 * @property brandWebClient WebClient para acesso dos End Points referentes a Marca.
 * @property brandDAO DAO responsável pelo acesso ao dados locais referentes a Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
class BrandRepository @Inject constructor(
    private val brandWebClient: BrandWebClient,
    private val brandDAO: BrandDAO
) {

    /**
     * Função responsável por salvar uma Marca localmente e remotamente.
     *
     * Caso ocorra algum erro ao salvar remotamente o registro será salvo
     * somente localmente para ser futuramente sincronizado.
     *
     * @param productId Id do produto ao qual está se atribuindo uma nova marca ou alterando uma existente
     * @param brandDomain Classe de domínio com as informações da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
//    suspend fun save(productId: UUID, brandDomain: BrandDomain): PersistenceResponse {
//        lateinit var brand: Brand
//        lateinit var productBrand: ProductBrand
//        lateinit var response: PersistenceResponse
//
//        if (brandDomain.id == null) {
//            brand = Brand(name = brandDomain.name)
//            productBrand = ProductBrand(productId = productId, brandId = brand.id, count = brandDomain.count)
//
//            response = brandWebClient.saveBrand(productId = productId, brand = brand, productBrand = productBrand)
//        } else {
//            brand = brandDAO.findByBrandId(brandDomain.id!!).copy(name = brandDomain.name)
//            productBrand = brandDAO.findProductBrandByBrandId(brand.id).copy(count = brandDomain.count)
//
//            response = brandWebClient.updateBrand(brand = brand, productBrand = productBrand)
//        }
//
//        brand.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE
//        productBrand.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE
//
//        brandDAO.saveBrand(brand)
//        brandDAO.saveProductBrand(productBrand)
//
//        /**
//         * Fazendo isso para que quando não conseguir se conectar com o servidor retorne sucesso
//         * por conta da persistência local
//         */
//        response.success = response.success || response.code == HttpURLConnection.HTTP_UNAVAILABLE
//
//        response = response.copy(idLocal = brand.id, idRemote = response.idRemote)
//
//        return response
//    }

    /**
     * Função responsável por realizar a exclusão de uma marca.
     *
     * Primeiramente os registros são inativados, em seguida,
     * tenta-se realizar a operação de exclusão dos registros no
     * serviço, caso obtiver sucesso, ai sim são excluídos localmente.
     *
     * @param localBrandId Id da marca
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun deleteBrand(localBrandId: UUID): MarketServiceResponse {
        brandDAO.inactivateBrandAndReferences(localBrandId)

        val response = brandWebClient.deleteBrand(localBrandId)

        if (response.success) {
            brandDAO.deleteBrandAndReferences(localBrandId)
        }

        // Fazendo isso para que quando não conseguir se conectar com o servidor retorne sucesso
        // por conta da inativação local
        response.success = response.success || response.code == HttpURLConnection.HTTP_UNAVAILABLE

        return response
    }

    /**
     * Função responsável por sincronizar as informações das Marcas das bases de dados.
     *
     * Primeiramente são recuperados todos os registros que não estão sincronizados mas estão
     * ativos. Esses registros serão enviados para serem persistidos ou alterados na base remota,
     * se ocorrer sucesso, são marcados como sincronizados na base lcoal.
     *
     * Depois são recuperados registros que foram inativados mas não foram sincronizados. Esses registros
     * serão enviados para o serviço para que sejam excluídos, se o retorno for sucesso, serão excluídos da base
     * local também.
     *
     * @author Nikolas Luiz Schmitt
     */
//    suspend fun syncBrands(): MarketServiceResponse {
//        val activeBrandsToSync = brandDAO.findAllActiveBrandsNotSynchronized()
//        val activeProductsBrandsToSync = brandDAO.findAllActiveProductsBrandsNotSynchronized()
//
//        val syncResponse = brandWebClient.syncBrands(activeBrandsToSync, activeProductsBrandsToSync)
//
//        if (syncResponse.success) {
//            activeBrandsToSync.forEach {
//                val brand = it.copy(synchronized = true)
//                brandDAO.saveBrand(brand)
//            }
//
//            activeProductsBrandsToSync.forEach {
//                val productBrand = it.copy(synchronized = true)
//                brandDAO.saveProductBrand(productBrand)
//            }
//
//            val inactiveAndNotSynchronizedBrands = brandDAO.findAllInactiveAndNotSynchronizedBrands()
//
//            val deleteResponse = brandWebClient.deleteBrands(inactiveAndNotSynchronizedBrands)
//
//            return if (deleteResponse.success) {
//                inactiveAndNotSynchronizedBrands.forEach {
//                    brandDAO.deleteBrandAndReferences(it.id)
//                }
//
//                val findAllBrandsResponse = brandWebClient.findAllBrands()
//                findAllBrandsResponse.values.forEach { brandDAO.saveBrand(it) }
//
//                return if (findAllBrandsResponse.success) {
//                    val findAllProductBrandsResponse = brandWebClient.findAllProductBrands()
//                    findAllProductBrandsResponse.values.forEach { brandDAO.saveProductBrand(it) }
//
//                    findAllProductBrandsResponse.toBaseResponse()
//                } else {
//                    findAllBrandsResponse.toBaseResponse()
//                }
//            } else {
//                deleteResponse
//            }
//
//        } else {
//            return syncResponse
//        }
//    }

    /**
     * Função para recuperar todos os ProductBrand ativos pelo id de
     * um Product específico.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
//    fun findAllActiveProductBrandsByProductId(productId: UUID?): Flow<List<ProductBrandDomain>> {
//        return brandDAO.findAllActiveProductBrandsByProductId(productId)
//    }

}