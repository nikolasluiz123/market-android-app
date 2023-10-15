package br.com.market.market.common.mediator.lov

import br.com.market.domain.UserDomain
import br.com.market.localdataaccess.dao.remotekeys.UserRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.BaseRemoteMediator
import br.com.market.models.keys.UserRemoteKeys
import br.com.market.sdo.UserSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse

class UserLovRemoteMediator(
    database: AppDatabase,
    remoteKeysDAO: UserRemoteKeysDAO
): BaseRemoteMediator<UserDomain, UserRemoteKeys, UserSDO>(database) {
    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<UserSDO> {
        TODO("Not yet implemented")
    }

    override suspend fun onLoadDataRefreshType() {
        TODO("Not yet implemented")
    }

    override suspend fun onSaveDataCache(response: ReadResponse<UserSDO>, remoteKeys: List<UserRemoteKeys>) {
        TODO("Not yet implemented")
    }
    override fun getRemoteKeysFromServiceData(ids: List<String>, prevKey: Int?, nextKey: Int?, currentPage: Int): List<UserRemoteKeys> {
        TODO("Not yet implemented")
    }

    override suspend fun getCreationTime(): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteKeyByID(id: String): UserRemoteKeys? {
        TODO("Not yet implemented")
    }
}