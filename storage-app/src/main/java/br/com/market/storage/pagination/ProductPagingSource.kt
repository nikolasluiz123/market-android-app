package br.com.market.storage.pagination

import br.com.market.core.pagination.BasePagingSource
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.tuples.ProductImageTuple

class ProductPagingSource(
    private val dao: ProductDAO,
    private val categoryId: String,
    private val brandId: String
) : BasePagingSource<ProductImageTuple>() {

    override suspend fun getData(limit: Int, offset: Int): List<ProductImageTuple> {
        return dao.findProducts(categoryId = categoryId, brandId = brandId, limit = limit, offset = offset)
    }
}