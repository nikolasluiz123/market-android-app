package br.com.market.market.common.repository.lov

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.base.BaseSearchFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.MarketLovDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.remotekeys.MarketRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.lov.MarketLovRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.servicedataaccess.webclients.MarketWebClient
import javax.inject.Inject

class MarketLovRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val marketRemoteKeysDAO: MarketRemoteKeysDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val companyDAO: CompanyDAO,
    private val marketWebClient: MarketWebClient
) : BaseRepository(), IPagedRemoteSearchRepository<BaseSearchFilter, MarketLovDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(context: Context, filters: BaseSearchFilter): Pager<Int, MarketLovDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { marketDAO.findMarketsLov(filters) },
            remoteMediator = MarketLovRemoteMediator(
                appDatabase, context, marketRemoteKeysDAO, marketDAO, addressDAO,
                companyDAO, marketWebClient, filters.quickFilter
            )
        )
    }
}