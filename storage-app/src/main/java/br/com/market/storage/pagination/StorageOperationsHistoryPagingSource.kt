package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.localdataaccess.tuples.StorageOperationHistoryTuple

class StorageOperationsHistoryPagingSource(
    private val dao: StorageOperationsHistoryDAO,
    private val productId: String?,
    private val categoryId: String,
    private val brandId: String,
    private val simpleFilter: String?
) : BasePagingSource<StorageOperationHistoryTuple>() {

    override suspend fun getData(limit: Int, offset: Int): List<StorageOperationHistoryTuple> {
        return dao.findStorageOperationsHistory(
            limit = limit,
            offset = offset,
            productId = productId,
            categoryId = categoryId,
            brandId = brandId,
            simpleFilter = simpleFilter
        )
    }
}