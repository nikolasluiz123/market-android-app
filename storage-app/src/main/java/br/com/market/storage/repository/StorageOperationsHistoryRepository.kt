package br.com.market.storage.repository

import br.com.market.domain.StorageOperationsHistoryDomain
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.models.StorageOperationHistory
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.webclients.StorageOperationsHistoryWebClient
import javax.inject.Inject

class StorageOperationsHistoryRepository @Inject constructor(
    private val dao: StorageOperationsHistoryDAO,
    private val webClient: StorageOperationsHistoryWebClient
) {

    suspend fun save(domain: StorageOperationsHistoryDomain): PersistenceResponse {
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
            StorageOperationHistory(
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
        val responseFindAllProducts = webClient.findAllStorageOperationsHistory()

        if (responseFindAllProducts.success) {
            dao.save(responseFindAllProducts.values)
        }

        return responseFindAllProducts.toBaseResponse()
    }
}