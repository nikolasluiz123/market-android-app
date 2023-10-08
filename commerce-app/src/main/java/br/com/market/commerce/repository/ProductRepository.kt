package br.com.market.commerce.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.commerce.mediator.ProductsRemoteMediator
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.tuples.ProductImageTuple
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDAO: ProductDAO,
    private val mediator: ProductsRemoteMediator
) {

    @OptIn(ExperimentalPagingApi::class)
    fun findProducts(): Pager<Int, ProductImageTuple> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { productDAO.findProductsToSell() },
            remoteMediator = mediator
        )
    }
}