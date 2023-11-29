package br.com.market.market.common.mediator

import android.content.Context
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.remotekeys.BrandRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.models.Brand
import br.com.market.models.CategoryBrand
import br.com.market.models.keys.BrandRemoteKeys
import br.com.market.sdo.BrandAndReferencesSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.webclients.BrandWebClient

class BrandRemoteMediator(
    database: AppDatabase,
    context: Context,
    private val remoteKeysDAO: BrandRemoteKeysDAO,
    private val brandDAO: BrandDAO,
    private val brandWebClient: BrandWebClient,
    private val marketId: Long,
    private val categoryId: String?,
    private val simpleFilter: String?
) : BaseRemoteMediator<BrandDomain, BrandRemoteKeys, BrandAndReferencesSDO>(context, database) {

    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<BrandAndReferencesSDO> {
        return brandWebClient.getListBrand(
            simpleFilter = simpleFilter, marketId = marketId, categoryLocalId = categoryId, limit = limit, offset = offset
        )
    }

    override suspend fun onLoadDataRefreshType() {
        remoteKeysDAO.clearRemoteKeys()
        brandDAO.clearAll()
    }

    override suspend fun onSaveDataCache(response: ReadResponse<BrandAndReferencesSDO>, remoteKeys: List<BrandRemoteKeys>) {
        remoteKeysDAO.insertAll(remoteKeys)

        val brands = getBrandsFrom(response)
        brandDAO.saveBrands(brands)

        val categoryBrands = getCategoryBrandsFrom(response)
        brandDAO.saveCategoryBrands(categoryBrands)
    }

    override fun getRemoteKeysFromServiceData(ids: List<String>, prevKey: Int?, nextKey: Int?, currentPage: Int): List<BrandRemoteKeys> {
        return ids.map { id ->
            BrandRemoteKeys(id = id, prevKey = prevKey, currentPage = currentPage, nextKey = nextKey)
        }
    }

    override suspend fun getRemoteKeyByID(id: String): BrandRemoteKeys? {
        return remoteKeysDAO.getRemoteKeyByID(id)
    }

    override suspend fun getCreationTime(): Long? {
        return remoteKeysDAO.getCreationTime()
    }

    override fun getEntityLocalId(sdo: BrandAndReferencesSDO): String {
        return sdo.brand.localId
    }

    private fun getBrandsFrom(response: ReadResponse<BrandAndReferencesSDO>): List<Brand> = response.values.map {
        Brand(
            id = it.brand.localId,
            name = it.brand.name,
            synchronized = true,
            active = it.brand.active,
            marketId = it.brand.marketId
        )
    }

    private fun getCategoryBrandsFrom(response: ReadResponse<BrandAndReferencesSDO>): List<CategoryBrand> = response.values.map {
        CategoryBrand(
            id = it.categoryBrand.localId,
            categoryId = it.categoryBrand.localCategoryId,
            brandId = it.categoryBrand.localBrandId,
            synchronized = true,
            marketId = it.categoryBrand.marketId
        )
    }
}