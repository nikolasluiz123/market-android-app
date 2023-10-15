package br.com.market.market.common.mediator.lov

import br.com.market.domain.ProductImageReadDomain
import br.com.market.localdataaccess.dao.remotekeys.ProductRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.BaseRemoteMediator
import br.com.market.models.keys.ProductRemoteKeys
import br.com.market.sdo.ProductSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse

class ProductLovRemoteMediator(
    database: AppDatabase,
    remoteKeysDAO: ProductRemoteKeysDAO
): BaseRemoteMediator<ProductImageReadDomain, ProductRemoteKeys, ProductSDO>(database) {
    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<ProductSDO> {
        TODO("Not yet implemented")
    }

    override suspend fun onLoadDataRefreshType() {
        TODO("Not yet implemented")
    }

    override suspend fun onSaveDataCache(response: ReadResponse<ProductSDO>, remoteKeys: List<ProductRemoteKeys>) {
        TODO("Not yet implemented")
    }
    override fun getRemoteKeysFromServiceData(ids: List<String>, prevKey: Int?, nextKey: Int?, currentPage: Int): List<ProductRemoteKeys> {
        TODO("Not yet implemented")
    }

    override suspend fun getCreationTime(): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteKeyByID(id: String): ProductRemoteKeys? {
        TODO("Not yet implemented")
    }
}