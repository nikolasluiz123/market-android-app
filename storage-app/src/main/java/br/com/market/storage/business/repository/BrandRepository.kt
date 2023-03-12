package br.com.market.storage.business.repository

import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.ProductBrand
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import br.com.market.storage.business.webclient.BrandWebClient
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductBrandDomain
import br.com.market.storage.utils.TransformClassHelper
import kotlinx.coroutines.flow.Flow
import java.net.HttpURLConnection
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
    suspend fun save(productId: Long, brandDomain: BrandDomain): PersistenceResponse {
        val brand = Brand()
        TransformClassHelper.domainToModel(domain = brandDomain, model = brand)

        val brandId = brandDAO.saveBrand(brand)

        val productBrand = if (brand.id != null) {
            val pb = brandDAO.findProductBrandByBrandId(brand.id!!)
            pb.copy(count = brandDomain.count)
        } else {
            ProductBrand(
                productId = productId,
                brandId = brandId,
                count = brandDomain.count
            )
        }

        brandDAO.saveProductBrand(productBrand)

        var response = if (brand.id == null) {
            brand.id = brandId
            brandWebClient.saveBrand(productId = productId, brand = brand, productBrand = productBrand)
        } else {
            brandWebClient.updateBrand(brand = brand, productBrand = productBrand)
        }

        brand.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE
        productBrand.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE

        brandDAO.saveBrand(brand)
        brandDAO.saveProductBrand(productBrand)

        /**
         * Fazendo isso para que quando não conseguir se conectar com o servidor retorne sucesso
         * por conta da persistência local
         */
        response.success = response.success || response.code == HttpURLConnection.HTTP_UNAVAILABLE

        response = response.copy(idLocal = brandId, idRemote = response.idRemote)

        return response
    }

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
    suspend fun deleteBrand(localBrandId: Long): MarketServiceResponse {
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
    suspend fun syncBrands(): MarketServiceResponse {
        val activeBrandsToSync = brandDAO.findAllActiveBrandsNotSynchronized()
        val activeProductsBrandsToSync = brandDAO.findAllActiveProductsBrandsNotSynchronized()

        val syncResponse = brandWebClient.syncBrands(activeBrandsToSync, activeProductsBrandsToSync)

        if (syncResponse.success) {
            activeBrandsToSync.forEach {
                val brand = it.copy(synchronized = true)
                brandDAO.saveBrand(brand)
            }

            activeProductsBrandsToSync.forEach {
                val productBrand = it.copy(synchronized = true)
                brandDAO.saveProductBrand(productBrand)
            }

            val inactiveAndNotSynchronizedBrands = brandDAO.findAllInactiveAndNotSynchronizedBrands()

            val deleteResponse = brandWebClient.deleteBrands(inactiveAndNotSynchronizedBrands)

            if (deleteResponse.success) {
                inactiveAndNotSynchronizedBrands.forEach {
                    brandDAO.deleteBrandAndReferences(it.id!!)
                }
            }

            return deleteResponse

        } else {
            return syncResponse
        }
    }

    /**
     * Função para recuperar todos os ProductBrand ativos pelo id de
     * um Product específico.
     *
     * @param productId Id do produto.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findAllActiveProductBrandsByProductId(productId: Long?): Flow<List<ProductBrandDomain>> {
        return brandDAO.findAllActiveProductBrandsByProductId(productId)
    }

}