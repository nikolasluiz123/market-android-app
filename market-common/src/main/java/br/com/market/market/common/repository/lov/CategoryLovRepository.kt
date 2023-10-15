package br.com.market.market.common.repository.lov

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.CategoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.lov.CategoryLovRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.servicedataaccess.webclients.CategoryWebClient
import javax.inject.Inject

class CategoryLovRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val categoryRemoteKeysDAO: CategoryRemoteKeysDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val companyDAO: CompanyDAO,
    private val categoryDAO: CategoryDAO,
    private val categoryWebClient: CategoryWebClient
) : BaseRepository(), IPagedRemoteSearchRepository<BaseSearchFilter, CategoryDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(filters: BaseSearchFilter): Pager<Int, CategoryDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { categoryDAO.findCategoriesLov(filters) },
            remoteMediator = CategoryLovRemoteMediator(
                appDatabase, categoryRemoteKeysDAO, marketDAO, addressDAO,
                companyDAO, categoryDAO, categoryWebClient, filters.marketId!!,
                filters.simpleFilter
            )
        )
    }
}