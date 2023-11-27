package br.com.market.storage.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.CategoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.CategoryRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.models.Category
import br.com.market.sdo.CategorySDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
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
    private val categoryDAO: CategoryDAO,
    private val categoryWebClient: CategoryWebClient,
) : BaseRepository(), IPagedRemoteSearchRepository<BaseSearchFilter, CategoryDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(context: Context, filters: BaseSearchFilter): Pager<Int, CategoryDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { categoryDAO.findCategories(filters) },
            remoteMediator = CategoryRemoteMediator(
                database = appDatabase,
                context = context,
                remoteKeysDAO = categoryRemoteKeysDAO,
                categoryDAO = categoryDAO,
                categoryWebClient = categoryWebClient,
                marketId = filters.marketId!!,
                simpleFilter = filters.simpleFilter
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
     *
     */
    suspend fun save(domain: CategoryDomain): PersistenceResponse {
        return if (domain.id != null) {
            editCategory(domain)
        } else {
            createCategory(domain)
        }
    }

    private suspend fun editCategory(domain: CategoryDomain): PersistenceResponse {
        val findResponse = categoryWebClient.findCategoryByLocalId(domain.id!!)

        return if (findResponse.success) {
            val category = getCategoryWithUpdatedInfo(findResponse, domain)
            saveCategory(category)
        } else {
            PersistenceResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, error = findResponse.error)
        }
    }

    private suspend fun getCategoryWithUpdatedInfo(findResponse: SingleValueResponse<CategorySDO>, domain: CategoryDomain): Category {
        return findResponse.value!!.copy(name = domain.name).run {
            Category(
                id = localId,
                name = domain.name,
                marketId = marketDAO.findFirst().first()?.id!!
            )
        }
    }

    private suspend fun saveCategory(category: Category): PersistenceResponse {
        val persistenceResponse = categoryWebClient.save(category = category)

        return if (persistenceResponse.success) {
            categoryDAO.save(category = category)
            PersistenceResponse(code = HttpURLConnection.HTTP_OK, success = true)
        } else {
            PersistenceResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, error = persistenceResponse.error)
        }
    }

    private suspend fun createCategory(domain: CategoryDomain): PersistenceResponse {
        val category = Category(name = domain.name, marketId = marketDAO.findFirst().first()?.id!!)
        return saveCategory(category)
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
    suspend fun toggleActive(categoryId: String, active: Boolean): PersistenceResponse {
        val response = categoryWebClient.toggleActive(categoryId)

        if (response.success) {
            categoryDAO.updateActive(categoryId = categoryId, active = !active, sync = true)
        }

        return response
    }
}