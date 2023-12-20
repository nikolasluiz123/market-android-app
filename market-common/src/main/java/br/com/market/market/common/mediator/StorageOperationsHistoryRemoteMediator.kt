package br.com.market.market.common.mediator

import android.content.Context
import br.com.market.domain.StorageOperationHistoryReadDomain
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.models.keys.StorageOperationHistoryRemoteKeys
import br.com.market.sdo.StorageOperationHistorySDO
import br.com.market.servicedataaccess.responses.types.ReadResponse

class StorageOperationsHistoryRemoteMediator(
    context: Context,
    database: AppDatabase,
): BaseRemoteMediator<StorageOperationHistoryReadDomain, StorageOperationHistoryRemoteKeys, StorageOperationHistorySDO>(context, database) {

    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<StorageOperationHistorySDO> {
        TODO("Not yet implemented")
    }

    override suspend fun onLoadDataRefreshType() {
        TODO("Not yet implemented")
    }

    override fun getRemoteKeysFromServiceData(
        ids: List<String>,
        prevKey: Int?,
        nextKey: Int?,
        currentPage: Int
    ): List<StorageOperationHistoryRemoteKeys> {
        TODO("Not yet implemented")
    }

    override suspend fun getCreationTime(): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteKeyByID(id: String): StorageOperationHistoryRemoteKeys? {
        TODO("Not yet implemented")
    }

    override suspend fun onSaveDataCache(response: ReadResponse<StorageOperationHistorySDO>, remoteKeys: List<StorageOperationHistoryRemoteKeys>) {
        TODO("Not yet implemented")
    }
}