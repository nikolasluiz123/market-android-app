package br.com.market.storage.repository.brand

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.models.Brand
import br.com.market.models.CategoryBrand
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.webclients.BrandWebClient
import br.com.market.storage.pagination.BrandPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Classe Repository responsável pela manipulação de dados
 * referentes a Marca.
 *
 * @property webClient WebClient para acesso dos End Points referentes a Marca.
 * @property brandDAO DAO responsável pelo acesso ao dados locais referentes a Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
class BrandRepository @Inject constructor(
    private val brandDAO: BrandDAO,
    private val marketDAO: MarketDAO,
    private val webClient: BrandWebClient
) {

    /**
     * Função para obter um fluxo de dados paginados que possa ser
     * fornecido a tela de listagem de marcas
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findBrands(categoryId: String? = null, brandName: String? = null): Flow<PagingData<BrandDomain>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { BrandPagingSource(brandDAO, categoryId, brandName) }
        ).flow
    }

    /**
     * Função que persiste uma marca nas bases local e remota.
     *
     * Primeiro tenta persistir na base remota, sendo sucesso,
     * define a marca como sincronizada e persiste também na base
     * local.
     *
     * Se ocorrer algum erro na persistência remota, a marca será
     * salva localmente e marcada como não sincronizada para futura
     * sincronização.
     *
     * @param domain Classe de domínio com as informações que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun save(categoryId: String, domain: BrandDomain): PersistenceResponse {
        val market = marketDAO.findFirst().first()?.id!!

        val brand = if (domain.id != null) {
            brandDAO.findBrandById(domain.id!!).copy(name = domain.name)
        } else {
            Brand(name = domain.name, marketId = market)
        }

        val categoryBrand = brandDAO.findCategoryBrandBy(
            brandId = brand.id,
            categoryId = categoryId
        ) ?: CategoryBrand(
            categoryId = categoryId,
            brandId = brand.id,
            marketId = market
        )

        domain.id = brand.id

        val response = webClient.save(brand = brand, categoryBrand = categoryBrand)

        val objectSynchronized = response.getObjectSynchronized()
        brand.synchronized = objectSynchronized
        categoryBrand.synchronized = objectSynchronized

        brandDAO.saveBrandAndReference(brand = brand, categoryBrand = categoryBrand)

        return response
    }

    /**
     * Função para buscar uma marca com um ID específico.
     *
     * @param brandId Id da marca que deseja buscar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun findById(brandId: String): BrandDomain {
        val brand = brandDAO.findBrandById(brandId)

        return BrandDomain(
            id = brand.id,
            name = brand.name!!,
            active = brand.active,
            marketId = brand.marketId,
            synchronized = brand.synchronized
        )
    }

    suspend fun findCategoryBrandActive(categoryId: String, brandId: String): Boolean {
        return brandDAO.findCategoryBrandBy(brandId = brandId, categoryId = categoryId)?.active!!
    }

    /**
     * Função para alterar a flag [Brand.active] nas bases local e remota.
     *
     * Primeiro tenta alterar na base remota, sendo sucesso,
     * define a marca como sincronizada e altera também na base
     * local.
     *
     * Se ocorrer algum erro na alteração remota, a marca será
     * alerada localmente e marcada como não sincronizada para futura
     * sincronização.
     *
     * @param brandId Id da marca que deseja reativar ou inativar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(brandId: String, categoryId: String): PersistenceResponse {
        val categoryBrand = brandDAO.findCategoryBrandBy(brandId = brandId, categoryId = categoryId)!!

        val response = webClient.toggleActive(categoryBrand)

        categoryBrand.synchronized = response.getObjectSynchronized()

        brandDAO.toggleActive(categoryBrand)

        return response
    }

    /**
     * Função para sincronizar os dados locais e remotos.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun sync(): MarketServiceResponse {
        val response = sendBrandsToRemoteDB()
        return if (response.success) updateBrandsOfLocalDB() else response
    }

    /**
     * Função para enviar as marcas para o banco remoto
     *
     * @author Nikolas Luiz Schmitt
     */
    private suspend fun sendBrandsToRemoteDB(): MarketServiceResponse {
        val brandsNotSynchronized = brandDAO.findBrandsNotSynchronized()
        val categoryBrandsNotSynchronized = brandDAO.findCategoryBrandsNotSynchronized()
        val response = webClient.sync(brandsNotSynchronized, categoryBrandsNotSynchronized)

        if (response.success) {
            val brandsSynchronized = brandsNotSynchronized.map { it.copy(synchronized = true) }
            brandDAO.saveBrandsAndReferences(brandsSynchronized, categoryBrandsNotSynchronized)
        }

        return response
    }

    /**
     * Função para buscar as marcas da base remota e cadastrar ou alterar
     * elas na base local
     *
     * @author Nikolas Luiz Schmitt
     */
    private suspend fun updateBrandsOfLocalDB(): MarketServiceResponse {
        val marketId = marketDAO.findFirst().first()?.id!!

        val responseFindAllBrands = webClient.findAllBrands(marketId)
        if (!responseFindAllBrands.success) {
            return responseFindAllBrands.toBaseResponse()
        }

        val responseFindAllCategoryBrands = webClient.findAllCategoryBrands(marketId)
        if (!responseFindAllCategoryBrands.success) {
            return responseFindAllCategoryBrands.toBaseResponse()
        }

        brandDAO.saveBrandsAndReferences(responseFindAllBrands.values, responseFindAllCategoryBrands.values)

        return responseFindAllBrands.toBaseResponse()
    }

}