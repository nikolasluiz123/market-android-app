package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO

class CategoryPagingSource(private val categoryDAO: CategoryDAO) : BasePagingSource<CategoryDomain>() {

    override suspend fun getData(offset: Int, limit: Int): List<CategoryDomain> {
        return categoryDAO.findCategories(offset, limit)
    }
}