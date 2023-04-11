package br.com.market.storage.repository

import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDAO: CategoryDAO
) {

    fun findCategories(): Flow<List<CategoryDomain>> {
        return categoryDAO.findCategories()
    }
}