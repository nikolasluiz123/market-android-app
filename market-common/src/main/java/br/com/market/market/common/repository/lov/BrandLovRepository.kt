package br.com.market.market.common.repository.lov

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.BrandRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.lov.BrandLovRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.servicedataaccess.webclients.BrandWebClient
import javax.inject.Inject

class BrandLovRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val brandRemoteKeysDAO: BrandRemoteKeysDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val companyDAO: CompanyDAO,
    private val categoryDAO: CategoryDAO,
    private val brandDAO: BrandDAO,
    private val brandWebClient: BrandWebClient
) : BaseRepository(), IPagedRemoteSearchRepository<BaseSearchFilter, BrandDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(filters: BaseSearchFilter): Pager<Int, BrandDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { brandDAO.findBrandsLov(filters) },
            remoteMediator = BrandLovRemoteMediator(
                appDatabase, brandRemoteKeysDAO, marketDAO, addressDAO,
                companyDAO, categoryDAO, brandDAO, brandWebClient, filters.marketId!!,
                filters.simpleFilter
            )
        )
    }
}