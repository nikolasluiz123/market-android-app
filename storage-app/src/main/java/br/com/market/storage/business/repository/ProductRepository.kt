package br.com.market.storage.business.repository

import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.mappers.ProductMapper
import br.com.market.storage.extensions.toProductDomain
import br.com.market.storage.extensions.toProductDomainList
import br.com.market.storage.ui.domains.ProductDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDAO: ProductDAO,
    private val brandDAO: BrandDAO
) {

    suspend fun save(productDomain: ProductDomain): Long {
        val product = ProductMapper.toProductModel(productDomain)
        return productDAO.saveProduct(product)
    }

    fun findAllProducts(): Flow<List<ProductDomain>> {
        return productDAO.findAllProducts().toProductDomainList()
    }

    fun findProductById(productId: Long?): Flow<ProductDomain?> {
        return productDAO.findProductById(productId).toProductDomain()
    }

    suspend fun deleteProduct(id: Long) {
        productDAO.deleteProductAndReferences(id)
    }

    suspend fun deleteBrandAndReferences(productId: Long) {
        brandDAO.deleteBrandAndReferences(productId)
    }
}