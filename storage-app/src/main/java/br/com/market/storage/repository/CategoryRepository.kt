package br.com.market.storage.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.CategoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.CategoryRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.models.Category
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.webclients.CategoryWebClient
import kotlinx.coroutines.flow.first
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * Classe Repository responsável pela manipulação de dados
 * referentes a Categoria.
 *
 * @property webClient WebClient para acesso dos End Points referentes a Marca.
 * @property categoryDAO DAO responsável pelo acesso ao dados locais referentes a Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
class CategoryRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val categoryRemoteKeysDAO: CategoryRemoteKeysDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val companyDAO: CompanyDAO,
    private val categoryDAO: CategoryDAO,
    private val categoryWebClient: CategoryWebClient
): BaseRepository(), IPagedRemoteSearchRepository<BaseSearchFilter, CategoryDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(filters: BaseSearchFilter): Pager<Int, CategoryDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { categoryDAO.findCategories(filters) },
            remoteMediator = CategoryRemoteMediator(
                appDatabase, categoryRemoteKeysDAO, marketDAO, addressDAO,
                companyDAO, categoryDAO, categoryWebClient, filters.marketId!!,
                filters.simpleFilter
            )
        )
    }

    /**
     * Função que persiste uma categoria nas bases local e remota.
     *
     * Primeiro tenta persistir na base remota, sendo sucesso,
     * define a categoria como sincronizada e persiste também na base
     * local.
     *
     * Se ocorrer algum erro na persistência remota, a categoria será
     * salva localmente e marcada como não sincronizada para futura
     * sincronização.
     *
     * @param domain Classe de domínio com as informações que deseja salvar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun save(domain: CategoryDomain): PersistenceResponse {
        val category = if (domain.id != null) {
            categoryDAO.findById(domain.id!!).copy(name = domain.name)
        } else {
            Category(name = domain.name, marketId = marketDAO.findFirst().first()?.id!!)
        }

        domain.id = category.id

        val response = categoryWebClient.save(category = category)

        category.synchronized = response.getObjectSynchronized()
        categoryDAO.save(category = category)

        return  response
    }

    /**
     * Função para buscar uma categoria com um ID específico.
     *
     * @param categoryId Id da categoria que deseja buscar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun findById(categoryId: String): CategoryDomain {
        val category = categoryDAO.findById(categoryId)
        return CategoryDomain(id = category.id, name = category.name!!, active = category.active)
    }

    /**
     * Função para alterar a flag [Category.active] nas bases local e remota.
     *
     * Primeiro tenta alterar na base remota, sendo sucesso,
     * define a categoria como sincronizada e altera também na base
     * local.
     *
     * Se ocorrer algum erro na alteração remota, a categoria será
     * alerada localmente e marcada como não sincronizada para futura
     * sincronização.
     *
     * @param categoryId Id da categoria que deseja reativar ou inativar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun toggleActive(categoryId: String): PersistenceResponse {
        val category = categoryDAO.findById(categoryId)

        val response = categoryWebClient.toggleActive(category)

        category.synchronized = response.getObjectSynchronized()

        categoryDAO.toggleActive(category)

        return response
    }

    /**
     * Função para sincronizar os dados locais e remotos.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun sync(): MarketServiceResponse {
        val response = sendCategoriesToRemoteDB()
        return if (response.success) updateCategoriesOfLocalDB() else response
    }

    /**
     * Função para enviar as categorias para o banco remoto
     *
     * @author Nikolas Luiz Schmitt
     */
    private suspend fun sendCategoriesToRemoteDB(): MarketServiceResponse {
        val categoriesNotSynchronized = categoryDAO.findCategoriesNotSynchronized()
        val response = categoryWebClient.sync(categoriesNotSynchronized)

        if (response.success) {
            val categoriesSynchronized = categoriesNotSynchronized.map { it.copy(synchronized = true) }
            categoryDAO.save(categoriesSynchronized)
        }

        return response
    }

    /**
     * Função para buscar as categorias da base remota e cadastrar ou alterar
     * elas na base local
     *
     * @author Nikolas Luiz Schmitt
     */
    private suspend fun updateCategoriesOfLocalDB(): MarketServiceResponse {
//        val marketId = marketDAO.findFirst().first()?.id!!

//        return importPagingData(
//            onWebServiceFind = { limit, offset ->
//                webClient.findCategorySDOs(marketId = marketId, limit = limit, offset = offset)
//            },
//            onPersistData = categoryDAO::save
//        )

        return MarketServiceResponse(code = HttpURLConnection.HTTP_OK, success = true)
    }
}