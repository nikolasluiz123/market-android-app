package br.com.market.storage.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.BrandFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.remotekeys.BrandRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.BrandRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.models.Brand
import br.com.market.models.CategoryBrand
import br.com.market.sdo.BrandAndReferencesSDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import br.com.market.servicedataaccess.webclients.BrandWebClient
import kotlinx.coroutines.flow.first
import java.net.HttpURLConnection
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
    private val appDatabase: AppDatabase,
    private val brandRemoteKeysDAO: BrandRemoteKeysDAO,
    private val brandDAO: BrandDAO,
    private val marketDAO: MarketDAO,
    private val productDAO: ProductDAO,
    private val webClient: BrandWebClient
) : BaseRepository(), IPagedRemoteSearchRepository<BrandFilter, BrandDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(context: Context, filters: BrandFilter): Pager<Int, BrandDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { brandDAO.findBrands(filters) },
            remoteMediator = BrandRemoteMediator(
                database = appDatabase,
                context = context,
                remoteKeysDAO = brandRemoteKeysDAO,
                marketId = filters.marketId!!,
                simpleFilter = filters.quickFilter,
                brandDAO = brandDAO,
                productDAO = productDAO,
                brandWebClient = webClient,
                categoryId = filters.categoryId
            )
        )
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
        val brand = brandDAO.findBrandById(brandId = domain.id)
        val categoryBrand = brandDAO.findCategoryBrandBy(brandId = domain.id, categoryId = categoryId)

        return if (brand != null && categoryBrand != null) {
            editBrand(domain, brand, categoryBrand)
        } else {
            createBrand(domain, brand, categoryId)
        }
    }

    private suspend fun editBrand(domain: BrandDomain, brand: Brand, categoryBrand: CategoryBrand): PersistenceResponse {
        val updatedBrand = brand.copy(name = domain.name)
        return saveBrand(updatedBrand, categoryBrand)
    }

    private suspend fun createBrand(domain: BrandDomain, brand: Brand?, categoryId: String): PersistenceResponse {
        val marketId = marketDAO.findFirst().first()?.id!!

        val brandToCreate = brand ?: Brand(name = domain.name, marketId = marketId)

        val categoryBrand = CategoryBrand(categoryId = categoryId, brandId = brandToCreate.id, marketId = marketId)

        domain.id = brandToCreate.id

        return saveBrand(brandToCreate, categoryBrand)
    }

    private suspend fun saveBrand(brand: Brand, categoryBrand: CategoryBrand): PersistenceResponse {
        val persistenceResponse = webClient.save(brand = brand, categoryBrand = categoryBrand)

        return if (persistenceResponse.success) {
            brandDAO.saveBrandAndReference(brand = brand, categoryBrand = categoryBrand)
            PersistenceResponse(code = HttpURLConnection.HTTP_OK, success = true)
        } else {
            PersistenceResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, error = persistenceResponse.error)
        }
    }

    /**
     * Função para buscar uma marca com um ID específico.
     *
     * @param brandId Id da marca que deseja buscar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun cacheFindBrandDomainById(brandId: String): BrandDomain {
        val brand = brandDAO.findBrandById(brandId)!!

        return BrandDomain(
            id = brand.id, name = brand.name!!, active = brand.active, marketId = brand.marketId, synchronized = brand.synchronized
        )
    }

    suspend fun cacheFindCategoryBrandBy(categoryId: String, brandId: String): CategoryBrand? {
        return brandDAO.findCategoryBrandBy(categoryId = categoryId, brandId = brandId)
    }

    suspend fun cacheFindBrandAndReferencesBy(categoryId: String, brandId: String): SingleValueResponse<BrandAndReferencesSDO> {
        return webClient.findBrandByLocalId(categoryId, brandId)
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
        val response = webClient.toggleActive(categoryId = categoryId, brandId = brandId)

        if (response.success) {
            brandDAO.toggleActive(brandId = brandId, categoryId = categoryId)
        }

        return response
    }

}