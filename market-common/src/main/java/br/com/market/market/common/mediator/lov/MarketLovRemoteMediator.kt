package br.com.market.market.common.mediator.lov

import android.content.Context
import br.com.market.domain.MarketLovDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.MarketRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.BaseRemoteMediator
import br.com.market.models.Address
import br.com.market.models.Company
import br.com.market.models.Market
import br.com.market.models.ThemeDefinitions
import br.com.market.models.keys.MarketRemoteKeys
import br.com.market.sdo.MarketReadSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.webclients.MarketWebClient

class MarketLovRemoteMediator(
    database: AppDatabase,
    private val context: Context,
    private val remoteKeysDAO: MarketRemoteKeysDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val companyDAO: CompanyDAO,
    private val webClient: MarketWebClient,
    private val simpleFilter: String?
): BaseRemoteMediator<MarketLovDomain, MarketRemoteKeys, MarketReadSDO>(context, database) {
    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<MarketReadSDO> {
        return webClient.getListLovMarketReadSDO(simpleFilter, limit, offset)
    }

    override suspend fun onLoadDataRefreshType() {
        remoteKeysDAO.clearRemoteKeys()
        marketDAO.clearAll()
        addressDAO.clearAll()
        companyDAO.clearAll()
    }

    override suspend fun onSaveDataCache(response: ReadResponse<MarketReadSDO>, remoteKeys: List<MarketRemoteKeys>) {
        remoteKeysDAO.insertAll(remoteKeys)

        val themes = getThemesFrom(response)
        companyDAO.saveThemes(themes)

        val companies = getCompaniesFrom(response)
        companyDAO.saveCompanies(companies)

        val addresses = getAddressesFrom(response)
        addressDAO.saveAll(addresses)

        val markets = getMarketsFrom(response)
        marketDAO.saveAll(markets)
    }
    override fun getRemoteKeysFromServiceData(ids: List<String>, prevKey: Int?, nextKey: Int?, currentPage: Int): List<MarketRemoteKeys> {
        return ids.map { id ->
            MarketRemoteKeys(id = id, prevKey = prevKey, currentPage = currentPage, nextKey = nextKey)
        }
    }

    override suspend fun getCreationTime(): Long? {
        return remoteKeysDAO.getCreationTime()
    }

    override suspend fun getRemoteKeyByID(id: String): MarketRemoteKeys? {
        return remoteKeysDAO.getRemoteKeyByID(id)
    }

    private fun getThemesFrom(response: ReadResponse<MarketReadSDO>): List<ThemeDefinitions> =
        response.values.map {
            ThemeDefinitions(
                id = it.company.themeDefinitions.id,
                colorPrimary = it.company.themeDefinitions.colorPrimary,
                colorSecondary = it.company.themeDefinitions.colorSecondary,
                colorTertiary = it.company.themeDefinitions.colorTertiary,
                imageLogo = it.company.themeDefinitions.imageLogo
            )
        }

    private fun getCompaniesFrom(response: ReadResponse<MarketReadSDO>): List<Company> =
        response.values.map {
            Company(
                id = it.company.id,
                name = it.company.name,
                themeDefinitionsId = it.company.themeDefinitions.id
            )
        }

    private fun getAddressesFrom(response: ReadResponse<MarketReadSDO>): List<Address> =
        response.values.map {
            Address(
                id = it.address.localId,
                state = it.address.state,
                city = it.address.city,
                publicPlace = it.address.publicPlace,
                number = it.address.number,
                complement = it.address.complement,
                cep = it.address.cep,
                synchronized = true
            )
        }

    private fun getMarketsFrom(response: ReadResponse<MarketReadSDO>): List<Market> =
        response.values.map {
            Market(
                id = it.id,
                companyId = it.companyId,
                addressId = it.address.localId,
                name = it.name
            )
        }
}