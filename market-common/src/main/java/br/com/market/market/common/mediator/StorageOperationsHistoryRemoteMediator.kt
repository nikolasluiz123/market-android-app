package br.com.market.market.common.mediator

import android.content.Context
import br.com.market.domain.StorageOperationHistoryReadDomain
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.localdataaccess.dao.remotekeys.StorageOperationsHistoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.models.Product
import br.com.market.models.StorageOperationHistory
import br.com.market.models.keys.ProductRemoteKeys
import br.com.market.models.keys.StorageOperationHistoryRemoteKeys
import br.com.market.sdo.ProductAndReferencesSDO
import br.com.market.sdo.StorageOperationHistorySDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.params.StorageOperationsHistoryServiceSearchParams
import br.com.market.servicedataaccess.webclients.StorageOperationsHistoryWebClient

class StorageOperationsHistoryRemoteMediator(
    context: Context,
    database: AppDatabase,
    private val params: StorageOperationsHistoryServiceSearchParams,
    private val webClient: StorageOperationsHistoryWebClient,
    private val remoteKeyDAO: StorageOperationsHistoryRemoteKeysDAO,
    private val storageDAO: StorageOperationsHistoryDAO
): BaseRemoteMediator<StorageOperationHistoryReadDomain, StorageOperationHistoryRemoteKeys, StorageOperationHistorySDO>(context, database) {

    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<StorageOperationHistorySDO> {
        params.limit = limit
        params.offset = offset

        return webClient.getListStorageOperations(params)
    }

    override suspend fun onLoadDataRefreshType() {
        remoteKeyDAO.clearRemoteKeys()
        storageDAO.clearAll()
    }

    override fun getRemoteKeysFromServiceData(
        ids: List<String>,
        prevKey: Int?,
        nextKey: Int?,
        currentPage: Int
    ): List<StorageOperationHistoryRemoteKeys> {
        return ids.map { id ->
            StorageOperationHistoryRemoteKeys(id = id, prevKey = prevKey, currentPage = currentPage, nextKey = nextKey)
        }
    }

    override suspend fun getCreationTime(): Long? {
        return remoteKeyDAO.getCreationTime()
    }

    override suspend fun getRemoteKeyByID(id: String): StorageOperationHistoryRemoteKeys? {
        return remoteKeyDAO.getRemoteKeyByID(id)
    }

    override suspend fun onSaveDataCache(response: ReadResponse<StorageOperationHistorySDO>, remoteKeys: List<StorageOperationHistoryRemoteKeys>) {
        remoteKeyDAO.insertAll(remoteKeys)
        storageDAO.save(getOperationsFrom(response))
    }

    private fun getOperationsFrom(response: ReadResponse<StorageOperationHistorySDO>): List<StorageOperationHistory> = response.values.map {
        StorageOperationHistory(
            id = it.localId,
            dateRealization = it.dateRealization,
            datePrevision = it.datePrevision,
            operationType = it.operationType,
            description = it.description,
            userId = it.userId,
            productId = it.productId,
            quantity = it.quantity,
            active = it.active,
            marketId = it.marketId
        )
    }
}