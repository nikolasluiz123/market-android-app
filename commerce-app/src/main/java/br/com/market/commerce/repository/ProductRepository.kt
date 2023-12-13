package br.com.market.commerce.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.commerce.mediator.ProductsRemoteMediator
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.ProductImageReadDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.CategoryDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.remotekeys.ProductRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.servicedataaccess.webclients.ProductWebClient
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val database: AppDatabase,
    private val webClient: ProductWebClient,
    private val categoryDAO: CategoryDAO,
    private val brandDAO: BrandDAO,
    private val marketDAO: MarketDAO,
    private val companyDAO: CompanyDAO,
    private val productDAO: ProductDAO,
    private val remoteKeysDAO: ProductRemoteKeysDAO,
    private val addressDAO: AddressDAO,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun findProducts(simpleFilter: String? = null): Pager<Int, ProductImageReadDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { productDAO.findProductsToSell(simpleFilter) },
            remoteMediator = ProductsRemoteMediator(
                database = database,
                webClient = webClient,
                categoryDAO = categoryDAO,
                brandDAO = brandDAO,
                marketDAO = marketDAO,
                companyDAO = companyDAO,
                productDAO = productDAO,
                remoteKeysDAO = remoteKeysDAO,
                addressDAO = addressDAO,
                simpleFilter = simpleFilter
            )
        )
    }
}