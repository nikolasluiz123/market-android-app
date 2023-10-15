package br.com.market.market.common.mediator.lov

import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.BrandRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.BaseRemoteMediator
import br.com.market.models.Address
import br.com.market.models.Brand
import br.com.market.models.Category
import br.com.market.models.CategoryBrand
import br.com.market.models.Company
import br.com.market.models.Market
import br.com.market.models.ThemeDefinitions
import br.com.market.models.keys.BrandRemoteKeys
import br.com.market.sdo.BrandReadSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.webclients.BrandWebClient

class BrandLovRemoteMediator(
    database: AppDatabase,
    private val remoteKeysDAO: BrandRemoteKeysDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val companyDAO: CompanyDAO,
    private val categoryDAO: CategoryDAO,
    private val brandDAO: BrandDAO,
    private val brandWebClient: BrandWebClient,
    private val marketId: Long,
    private val simpleFilter: String?
) : BaseRemoteMediator<BrandDomain, BrandRemoteKeys, BrandReadSDO>(database) {

    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<BrandReadSDO> {
        return brandWebClient.getListBrandReadSDO(simpleFilter = simpleFilter, marketId = marketId, limit = limit, offset = offset)
    }

    override suspend fun onLoadDataRefreshType() {
        remoteKeysDAO.clearRemoteKeys()
        brandDAO.clearAll()
        categoryDAO.clearAll()
        marketDAO.clearAll()
        addressDAO.clearAll()
        companyDAO.clearAll()
    }

    override suspend fun onSaveDataCache(response: ReadResponse<BrandReadSDO>, remoteKeys: List<BrandRemoteKeys>) {
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

    private fun getThemesFrom(response: ReadResponse<BrandReadSDO>): List<ThemeDefinitions> =
        response.values.map {
            ThemeDefinitions(
                id = it.company.themeDefinitions.id,
                colorPrimary = it.company.themeDefinitions.colorPrimary,
                colorSecondary = it.company.themeDefinitions.colorSecondary,
                colorTertiary = it.company.themeDefinitions.colorTertiary,
                imageLogo = it.company.themeDefinitions.imageLogo
            )
        }

    private fun getCompaniesFrom(response: ReadResponse<BrandReadSDO>): List<Company> =
        response.values.map {
            Company(
                id = it.company.id,
                name = it.company.name,
                themeDefinitionsId = it.company.themeDefinitions.id
            )
        }

    private fun getAddressesFrom(response: ReadResponse<BrandReadSDO>): List<Address> =
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

    private fun getMarketsFrom(response: ReadResponse<BrandReadSDO>): List<Market> =
        response.values.map {
            Market(
                id = it.market.id,
                companyId = it.market.companyId,
                addressId = it.market.address?.localId,
                name = it.market.name
            )
        }

    private fun getCategoriesFrom(response: ReadResponse<BrandReadSDO>): List<Category> =
        response.values.map {
            Category(
                id = it.category.localId,
                name = it.category.name,
                synchronized = true,
                active = it.category.active,
                marketId = it.category.marketId
            )
        }

    private fun getBrandsFrom(response: ReadResponse<BrandReadSDO>): List<Brand> =
        response.values.map {
            Brand(
                id = it.localId,
                name = it.name,
                synchronized = true,
                active = it.active,
                marketId = it.marketId
            )
        }

    private fun getCategoryBrandsFrom(response: ReadResponse<BrandReadSDO>): List<CategoryBrand> =
        response.values.map {
            CategoryBrand(
                id = it.categoryBrand.localId,
                categoryId = it.categoryBrand.localCategoryId,
                brandId = it.categoryBrand.localBrandId,
                synchronized = true,
                marketId = it.categoryBrand.marketId
            )
        }
}