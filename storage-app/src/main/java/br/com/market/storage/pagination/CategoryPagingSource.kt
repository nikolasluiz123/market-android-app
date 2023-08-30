package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO

/**
 * PagingSource para listagem das categorias de forma paginada
 *
 * @property dao Classe de acesso aos dados locais da marca
 *
 * @author Nikolas Luiz Schmitt
 */
class CategoryPagingSource(
    private val dao: CategoryDAO,
    private val simpleFilterText: String? = null
) : BasePagingSource<CategoryDomain>() {

    override suspend fun getData(limit: Int, offset: Int): List<CategoryDomain> {
        return dao.findCategories(simpleFilterText = simpleFilterText, limit = limit, offset = offset)
    }
}