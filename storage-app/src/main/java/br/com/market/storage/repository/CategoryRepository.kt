package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.models.Category
import br.com.market.storage.pagination.CategoryPagingSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDAO: CategoryDAO
) {

    fun findCategories(): Flow<PagingData<CategoryDomain>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { CategoryPagingSource(categoryDAO) }
        ).flow
    }

    suspend fun saveCategory(categoryDomain: CategoryDomain) {
        val category = if (categoryDomain.id != null) {
            Category(id = categoryDomain.id!!, name = categoryDomain.name)
        } else {
            Category(name = categoryDomain.name)
        }

        categoryDomain.id = category.id

        categoryDAO.saveCategory(category)
    }

    suspend fun findById(categoryId: UUID): CategoryDomain = withContext(IO) {
        categoryDAO.findById(categoryId)
    }

    suspend fun toggleActive(categoryId: UUID, active: Boolean) {
        categoryDAO.toggleActive(categoryId, active)
    }
}