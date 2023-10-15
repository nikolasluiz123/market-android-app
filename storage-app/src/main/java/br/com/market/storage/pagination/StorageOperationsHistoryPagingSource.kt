package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.domain.StorageOperationHistoryReadDomain

class StorageOperationsHistoryPagingSource(
    private val dao: StorageOperationsHistoryDAO,
    private val productId: String?,
    private val categoryId: String,
    private val brandId: String,
    private val simpleFilter: String?,
    private val advancedFilter: MovementSearchScreenFilters
) : BasePagingSource<StorageOperationHistoryReadDomain>() {

    override suspend fun getData(limit: Int, offset: Int): List<StorageOperationHistoryReadDomain> {
        return dao.findStorageOperationsHistory(
            limit = limit,
            offset = offset,
            productId = productId,
            categoryId = categoryId,
            brandId = brandId,
            simpleFilter = simpleFilter,
            advancedFilter = advancedFilter
        )
    }
}