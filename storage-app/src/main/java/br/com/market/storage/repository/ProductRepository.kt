package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.ProductDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.ProductImageDAO
import br.com.market.localdataaccess.tuples.ProductImageTuple
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.servicedataaccess.responses.PersistenceResponse
import br.com.market.servicedataaccess.webclients.ProductWebClient
import br.com.market.storage.pagination.ProductPagingSource
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDAO: ProductDAO,
    private val productImageDAO: ProductImageDAO,
    private val brandDAO: BrandDAO,
    private val webClient: ProductWebClient
) {

    fun findProducts(categoryId: UUID, brandId: UUID): Flow<PagingData<ProductImageTuple>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { ProductPagingSource(dao = productDAO, categoryId = categoryId, brandId = brandId) }
        ).flow
    }

    suspend fun save(categoryId: UUID, brandId: UUID, domain: ProductDomain): PersistenceResponse {
        val product = if(domain.id != null) {
            productDAO.findProductById(productId = domain.id!!).copy(
                name = domain.name!!,
                price = domain.price!!,
                quantity = domain.quantity!!,
                quantityUnit = domain.quantityUnit,
            )
        } else {
            Product(
                name = domain.name!!,
                price = domain.price!!,
                quantity = domain.quantity!!,
                quantityUnit = domain.quantityUnit,
                categoryBrandId = brandDAO.findCategoryBrandBy(brandId = brandId, categoryId = categoryId).id
            )
        }

        domain.id = product.id

        val productImages = domain.images.map {
            ProductImage(bytes = it, productId = product.id)
        }

        val response = webClient.save(product = product, images = productImages)

        val objectSynchronized = response.getObjectSynchronized()
        product.synchronized = objectSynchronized
        productImages.forEach { it.synchronized = objectSynchronized }

        productDAO.save(product = product)
        productImageDAO.saveNewImages(images = productImages)

        return response
    }
}