package br.com.market.market.common.mediator

import android.content.Context
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.remotekeys.CategoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.models.Category
import br.com.market.models.keys.CategoryRemoteKeys
import br.com.market.sdo.CategorySDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.webclients.CategoryWebClient

class CategoryRemoteMediator(
    database: AppDatabase,
    context: Context,
    private val remoteKeysDAO: CategoryRemoteKeysDAO,
    private val categoryDAO: CategoryDAO,
    private val categoryWebClient: CategoryWebClient,
    private val marketId: Long,
    private val simpleFilter: String?
): BaseRemoteMediator<CategoryDomain, CategoryRemoteKeys, CategorySDO>(context, database) {

    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<CategorySDO> {
        return categoryWebClient.getListCategory(simpleFilter, marketId, limit, offset)
    }

    override suspend fun onLoadDataRefreshType() {
        remoteKeysDAO.clearRemoteKeys()
        categoryDAO.clearAll()
    }

    override suspend fun onSaveDataCache(response: ReadResponse<CategorySDO>, remoteKeys: List<CategoryRemoteKeys>) {
        remoteKeysDAO.insertAll(remoteKeys)

        val categories = getCategoriesFrom(response)
        categoryDAO.save(categories)
    }

    override fun getRemoteKeysFromServiceData(ids: List<String>, prevKey: Int?, nextKey: Int?, currentPage: Int): List<CategoryRemoteKeys> {
        return ids.map { id ->
            CategoryRemoteKeys(id = id, prevKey = prevKey, currentPage = currentPage, nextKey = nextKey)
        }
    }

    override suspend fun getCreationTime(): Long? {
        return remoteKeysDAO.getCreationTime()
    }

    override suspend fun getRemoteKeyByID(id: String): CategoryRemoteKeys? {
        return remoteKeysDAO.getRemoteKeyByID(id)
    }

    private fun getCategoriesFrom(response: ReadResponse<CategorySDO>): List<Category> =
        response.values.map {
            Category(
                id = it.localId,
                name = it.name,
                synchronized = true,
                active = it.active,
                marketId = it.marketId
            )
        }
}