package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.models.Category
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.webclients.CategoryWebClient
import br.com.market.storage.pagination.CategoryPagingSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDAO: CategoryDAO,
    private val categoryWebClient: CategoryWebClient
) {

    fun findCategories(): Flow<PagingData<CategoryDomain>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { CategoryPagingSource(categoryDAO) }
        ).flow
    }

    suspend fun save(categoryDomain: CategoryDomain): PersistenceResponse {
        val category = if (categoryDomain.id != null) {
            categoryDAO.findById(categoryDomain.id!!).copy(name = categoryDomain.name)
        } else {
            Category(name = categoryDomain.name)
        }

        categoryDomain.id = category.id

        val response = categoryWebClient.save(category = category)

        category.synchronized = response.getObjectSynchronized()
        categoryDAO.save(category = category)

        return  response
    }

    suspend fun findById(categoryId: UUID): CategoryDomain = withContext(IO) {
        val category = categoryDAO.findById(categoryId)
        CategoryDomain(category.id, category.name!!, category.active)
    }

    suspend fun toggleActive(categoryLocalId: UUID): PersistenceResponse {
        val category = categoryDAO.findById(categoryLocalId)

        val response = categoryWebClient.toggleActive(category)

        category.synchronized = response.getObjectSynchronized()

        categoryDAO.toggleActive(category)

        return response
    }

    suspend fun sync(): MarketServiceResponse {
        val response = sendCategoriesToRemoteDB()
        return if (response.success) updateCategoriesOfLocalDB() else response
    }

    private suspend fun sendCategoriesToRemoteDB(): MarketServiceResponse {
        val categoriesNotSynchronized = categoryDAO.findCategoriesNotSynchronized()
        val response = categoryWebClient.sync(categoriesNotSynchronized)

        if (response.success) {
            val categoriesSynchronized = categoriesNotSynchronized.map { it.copy(synchronized = true) }
            categoryDAO.save(categoriesSynchronized)
        }

        return response
    }

    private suspend fun updateCategoriesOfLocalDB(): MarketServiceResponse {
        val response = categoryWebClient.findAll()

        if (response.success) {
            categoryDAO.save(response.values)
        }

        return response.toBaseResponse()
    }
}