package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.BrandDAO

/**
 * PagingSource para listagem das marcas de forma paginada
 *
 * @property dao Classe de acesso aos dados locais da marca
 *
 * @author Nikolas Luiz Schmitt
 */
class BrandPagingSource(private val dao: BrandDAO) : BasePagingSource<BrandDomain>() {

    override suspend fun getData(offset: Int, limit: Int): List<BrandDomain> {
        return dao.findBrands(offset, limit)
    }
}