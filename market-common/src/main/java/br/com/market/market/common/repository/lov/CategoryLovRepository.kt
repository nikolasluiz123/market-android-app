package br.com.market.market.common.repository.lov

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.base.BaseSearchFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.ProductImageDAO
import br.com.market.localdataaccess.dao.remotekeys.CategoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.CategoryRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.servicedataaccess.webclients.CategoryWebClient
import javax.inject.Inject

class CategoryLovRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val categoryRemoteKeysDAO: CategoryRemoteKeysDAO,
    private val categoryDAO: CategoryDAO,
    private val productDAO: ProductDAO,
    private val productImageDAO: ProductImageDAO,
    private val brandDAO: BrandDAO,
    private val categoryWebClient: CategoryWebClient
) : BaseRepository(), IPagedRemoteSearchRepository<BaseSearchFilter, CategoryDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(context: Context, filters: BaseSearchFilter): Pager<Int, CategoryDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { categoryDAO.findCategoriesLov(filters) },
            remoteMediator = CategoryRemoteMediator(
                database = appDatabase,
                context = context,
                remoteKeysDAO = categoryRemoteKeysDAO,
                categoryDAO = categoryDAO,
                brandDAO = brandDAO,
                productDAO = productDAO,
                productImageDAO = productImageDAO,
                categoryWebClient = categoryWebClient,
                marketId = filters.marketId!!,
                simpleFilter = filters.quickFilter
            )
        )
    }
}