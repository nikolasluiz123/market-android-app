package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.BrandDAO
import java.util.*

/**
 * PagingSource para listagem das marcas de forma paginada
 *
 * @property dao Classe de acesso aos dados locais da marca
 *
 * @author Nikolas Luiz Schmitt
 */
class BrandPagingSource(
    private val dao: BrandDAO,
    private val categoryId: UUID?
) : BasePagingSource<BrandDomain>() {

    override suspend fun getData(limit: Int, offset: Int): List<BrandDomain> {
        return dao.findBrands(categoryId = categoryId, limit = limit, offset = offset)
    }
}