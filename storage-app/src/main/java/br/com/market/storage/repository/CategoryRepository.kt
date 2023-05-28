package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.models.Category
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.webclients.CategoryWebClient
import br.com.market.storage.pagination.CategoryPagingSource
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

/**
 * Classe Repository responsável pela manipulação de dados
 * referentes a Categoria.
 *
 * @property webClient WebClient para acesso dos End Points referentes a Marca.
 * @property dao DAO responsável pelo acesso ao dados locais referentes a Marca.
 *
 * @author Nikolas Luiz Schmitt
 */
class CategoryRepository @Inject constructor(
    private val dao: CategoryDAO,
    private val webClient: CategoryWebClient
) {

    /**
     * Função para obter um fluxo de dados paginados que possa ser
     * fornecido a tela de listagem de categorias
     *
     * @author Nikolas Luiz Schmitt
     */
    fun findCategories(): Flow<PagingData<CategoryDomain>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { CategoryPagingSource(dao) }
        ).flow
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
            dao.findById(domain.id!!).copy(name = domain.name)
        } else {
            Category(name = domain.name)
        }

        domain.id = category.id

        val response = webClient.save(category = category)

        category.synchronized = response.getObjectSynchronized()
        dao.save(category = category)

        return  response
    }

    /**
     * Função para buscar uma categoria com um ID específico.
     *
     * @param brandId Id da categoria que deseja buscar
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun findById(categoryId: String): CategoryDomain {
        val category = dao.findById(categoryId)
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
        val category = dao.findById(categoryId)

        val response = webClient.toggleActive(category)

        category.synchronized = response.getObjectSynchronized()

        dao.toggleActive(category)

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
        val categoriesNotSynchronized = dao.findCategoriesNotSynchronized()
        val response = webClient.sync(categoriesNotSynchronized)

        if (response.success) {
            val categoriesSynchronized = categoriesNotSynchronized.map { it.copy(synchronized = true) }
            dao.save(categoriesSynchronized)
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
        val response = webClient.findAll()

        if (response.success) {
            dao.save(response.values)
        }

        return response.toBaseResponse()
    }
}