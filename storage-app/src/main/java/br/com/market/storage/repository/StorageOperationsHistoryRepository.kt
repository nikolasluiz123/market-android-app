package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.StorageOperationHistoryDomain
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.localdataaccess.tuples.StorageOperationHistoryTuple
import br.com.market.models.StorageOperationHistory
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.webclients.StorageOperationsHistoryWebClient
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.storage.pagination.StorageOperationsHistoryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StorageOperationsHistoryRepository @Inject constructor(
    private val dao: StorageOperationsHistoryDAO,
    private val marketDAO: MarketDAO,
    private val webClient: StorageOperationsHistoryWebClient
) {

    fun findStorageOperationHistory(
        productId: String?,
        categoryId: String,
        brandId: String,
        simpleFilter: String?,
        advancedFilter: MovementSearchScreenFilters
    ): Flow<PagingData<StorageOperationHistoryTuple>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = {
                StorageOperationsHistoryPagingSource(
                    dao = dao,
                    productId = productId,
                    categoryId = categoryId,
                    brandId = brandId,
                    simpleFilter = simpleFilter,
                    advancedFilter = advancedFilter
                )
            }
        ).flow
    }

    suspend fun findStorageOperationHistoryDomainById(id: String): StorageOperationHistoryDomain {
        return dao.findById(id).run {
            StorageOperationHistoryDomain(
                id = id,
                active = active,
                synchronized = synchronized,
                productId = productId,
                quantity = quantity!!,
                dateRealization = dateRealization,
                datePrevision = datePrevision,
                operationType = operationType,
                description = description,
                userId = userId
            )
        }
    }

    suspend fun save(domain: StorageOperationHistoryDomain): PersistenceResponse {
        val storageOperationHistory = if (domain.id != null) {
            dao.findById(domain.id!!).copy(
                productId = domain.productId,
                dateRealization = domain.dateRealization,
                datePrevision = domain.datePrevision,
                operationType = domain.operationType,
                description = domain.description,
                userId = domain.userId,
                synchronized = domain.synchronized,
                active = domain.active,
                quantity = domain.quantity
            )
        } else {
            val marketId = marketDAO.findFirst().first()?.id!!

            StorageOperationHistory(
                productId = domain.productId,
                dateRealization = domain.dateRealization,
                datePrevision = domain.datePrevision,
                operationType = domain.operationType,
                description = domain.description,
                userId = domain.userId,
                synchronized = domain.synchronized,
                active = domain.active,
                quantity = domain.quantity,
                marketId = marketId
            )
        }

        domain.id = storageOperationHistory.id

        val response = webClient.save(storageOperationHistory)
        storageOperationHistory.synchronized = response.getObjectSynchronized()

        dao.save(storageOperationHistory)

        return response
    }

    suspend fun sync(): MarketServiceResponse {
        val response = sendStorageOperationsHistoryToRemoteDB()
        return if (response.success) updateStorageOperationsHistoryOfLocalDB() else response
    }

    private suspend fun sendStorageOperationsHistoryToRemoteDB(): MarketServiceResponse {
        val storageOperationsHistoryNotSynchronized = dao.findStorageOperationsHistoryNotSynchronized()
        val response = webClient.sync(storageOperationsHistoryNotSynchronized)

        if (response.success) {
            val storageOperationsHistorySynchronized = storageOperationsHistoryNotSynchronized.map { it.copy(synchronized = true) }
            dao.save(storageOperationsHistorySynchronized)
        }

        return response
    }

    private suspend fun updateStorageOperationsHistoryOfLocalDB(): MarketServiceResponse {
        val responseFindAllProducts = webClient.findAllStorageOperationsHistory(marketDAO.findFirst().first()?.id!!)

        if (responseFindAllProducts.success) {
            dao.save(responseFindAllProducts.values)
        }

        return responseFindAllProducts.toBaseResponse()
    }

    suspend fun inactivate(id: String) {
        val response = webClient.inactivate(id)
        val synchronized = response.getObjectSynchronized()

        dao.inactivate(id = id, sync = synchronized)
    }
}