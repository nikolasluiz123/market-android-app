package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.tuples.ProductImageTuple

class ProductPagingSource(
    private val dao: ProductDAO,
    private val categoryId: String,
    private val brandId: String,
    private val simpleFilter: String?
) : BasePagingSource<ProductImageTuple>() {

    override suspend fun getData(limit: Int, offset: Int): List<ProductImageTuple> {
        return dao.findProducts(categoryId = categoryId, brandId = brandId, simpleFilter = simpleFilter, limit = limit, offset = offset)
    }
}