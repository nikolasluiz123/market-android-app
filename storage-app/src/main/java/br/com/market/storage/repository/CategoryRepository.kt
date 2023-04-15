package br.com.market.storage.repository

import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.models.Category
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDAO: CategoryDAO
) {

    fun findCategories(): Flow<List<CategoryDomain>> {
        return categoryDAO.findCategories()
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

    suspend fun inactivateCategory(categoryId: UUID) {
        categoryDAO.inactivateCategory(categoryId)
    }
}