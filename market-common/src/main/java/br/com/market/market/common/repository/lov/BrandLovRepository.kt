package br.com.market.market.common.repository.lov

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.BrandFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.BrandDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.remotekeys.BrandRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.BrandRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.servicedataaccess.webclients.BrandWebClient
import javax.inject.Inject

class BrandLovRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val brandRemoteKeysDAO: BrandRemoteKeysDAO,
    private val brandDAO: BrandDAO,
    private val productDAO: ProductDAO,
    private val brandWebClient: BrandWebClient
) : BaseRepository(), IPagedRemoteSearchRepository<BrandFilter, BrandDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(context: Context, filters: BrandFilter): Pager<Int, BrandDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { brandDAO.findBrandsLov(filters) },
            remoteMediator = BrandRemoteMediator(
                database = appDatabase,
                context = context,
                remoteKeysDAO = brandRemoteKeysDAO,
                brandDAO = brandDAO,
                productDAO = productDAO,
                brandWebClient = brandWebClient,
                marketId = filters.marketId!!,
                simpleFilter = filters.quickFilter,
                categoryId = filters.categoryId
            )
        )
    }
}