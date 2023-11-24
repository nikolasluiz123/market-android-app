package br.com.market.market.common.mediator

import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.CategoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.models.Address
import br.com.market.models.Category
import br.com.market.models.Company
import br.com.market.models.Market
import br.com.market.models.ThemeDefinitions
import br.com.market.models.keys.CategoryRemoteKeys
import br.com.market.sdo.CategoryReadSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.webclients.CategoryWebClient

class CategoryRemoteMediator(
    database: AppDatabase,
    private val remoteKeysDAO: CategoryRemoteKeysDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val companyDAO: CompanyDAO,
    private val categoryDAO: CategoryDAO,
    private val categoryWebClient: CategoryWebClient,
    private val marketId: Long,
    private val simpleFilter: String?
): BaseRemoteMediator<CategoryDomain, CategoryRemoteKeys, CategoryReadSDO>(database) {

    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<CategoryReadSDO> {
        return categoryWebClient.getListCategoryReadSDO(simpleFilter, marketId, limit, offset)
    }

    override suspend fun onLoadDataRefreshType() {
        remoteKeysDAO.clearRemoteKeys()
        categoryDAO.clearAll()
        marketDAO.clearAll()
        addressDAO.clearAll()
        companyDAO.clearAll()
    }

    override suspend fun onSaveDataCache(response: ReadResponse<CategoryReadSDO>, remoteKeys: List<CategoryRemoteKeys>) {
        remoteKeysDAO.insertAll(remoteKeys)

        val themes = getThemesFrom(response)
        companyDAO.saveThemes(themes)

        val companies = getCompaniesFrom(response)
        companyDAO.saveCompanies(companies)

        val addresses = getAddressesFrom(response)
        addressDAO.saveAll(addresses)

        val markets = getMarketsFrom(response)
        marketDAO.saveAll(markets)

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

    private fun getThemesFrom(response: ReadResponse<CategoryReadSDO>): List<ThemeDefinitions> =
        response.values.map {
            ThemeDefinitions(
                id = it.company.themeDefinitions.id,
                colorPrimary = it.company.themeDefinitions.colorPrimary,
                colorSecondary = it.company.themeDefinitions.colorSecondary,
                colorTertiary = it.company.themeDefinitions.colorTertiary,
                imageLogo = it.company.themeDefinitions.imageLogo
            )
        }

    private fun getCompaniesFrom(response: ReadResponse<CategoryReadSDO>): List<Company> =
        response.values.map {
            Company(
                id = it.company.id,
                name = it.company.name,
                themeDefinitionsId = it.company.themeDefinitions.id
            )
        }

    private fun getAddressesFrom(response: ReadResponse<CategoryReadSDO>): List<Address> =
        response.values.map {
            Address(
                id = it.market.address?.localId!!,
                state = it.market.address?.state,
                city = it.market.address?.city,
                publicPlace = it.market.address?.publicPlace,
                number = it.market.address?.number,
                complement = it.market.address?.complement,
                cep = it.market.address?.cep,
                synchronized = true
            )
        }

    private fun getMarketsFrom(response: ReadResponse<CategoryReadSDO>): List<Market> =
        response.values.map {
            Market(
                id = it.market.id,
                companyId = it.market.companyId,
                addressId = it.market.address?.localId,
                name = it.market.name
            )
        }

    private fun getCategoriesFrom(response: ReadResponse<CategoryReadSDO>): List<Category> =
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