package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.ProductDomain
import br.com.market.domain.ProductImageDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.ProductImageDAO
import br.com.market.market.common.repository.BaseRepository
import br.com.market.domain.ProductImageReadDomain
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.webclients.ProductWebClient
import br.com.market.storage.pagination.ProductPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDAO: ProductDAO,
    private val marketDAO: MarketDAO,
    private val productImageDAO: ProductImageDAO,
    private val brandDAO: BrandDAO,
    private val webClient: ProductWebClient
): BaseRepository() {

    fun findProducts(categoryId: String, brandId: String, simpleFilter: String?): Flow<PagingData<ProductImageReadDomain>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = {
                ProductPagingSource(
                    dao = productDAO,
                    categoryId = categoryId,
                    brandId = brandId,
                    simpleFilter = simpleFilter
                )
            }
        ).flow
    }

    suspend fun findProductDomain(productId: String): ProductDomain {
        val product = productDAO.findProductById(productId)
        val images = productImageDAO.findProductImagesBy(productId)

        return ProductDomain(
            id = product.id,
            active = product.active,
            synchronized = product.synchronized,
            name = product.name,
            price = product.price,
            quantity = product.quantity,
            quantityUnit = product.quantityUnit,
            images = images.map {
                ProductImageDomain(
                    id = it.id,
                    active = it.active,
                    marketId = it.marketId,
                    synchronized = it.synchronized,
                    byteArray = it.bytes,
                    productId = it.productId,
                    principal = it.principal
                )
            }.toMutableList()
        )
    }

    suspend fun findProductImageDomain(productImageId: String): ProductImageDomain? {
        return productImageDAO.findProductImageBy(productImageId)?.run {
            ProductImageDomain(
                id = id,
                active = active,
                marketId = marketId,
                synchronized = synchronized,
                byteArray = bytes,
                productId = productId,
                principal = principal
            )
        }
    }

    suspend fun saveProduct(categoryId: String, brandId: String, domain: ProductDomain): PersistenceResponse {
        val marketId = marketDAO.findFirst().first()?.id!!

        val product = if (domain.id != null) {
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
                categoryBrandId = brandDAO.findCategoryBrandBy(brandId = brandId, categoryId = categoryId)!!.id,
                marketId = marketId
            )
        }

        domain.id = product.id

        val productImages = domain.images.map {
            productImageDAO.findProductImageBy(it.id)?.copy(
                active = it.active,
                marketId = it.marketId,
                synchronized = it.synchronized,
                bytes = it.byteArray,
                productId = product.id,
                principal = it.principal
            ) ?: ProductImage(
                active = it.active,
                marketId = marketId,
                synchronized = it.synchronized,
                bytes = it.byteArray,
                productId = product.id,
                principal = it.principal
            )
        }

        domain.images.forEach { imageDomain ->
            productImages.forEach { imageModel ->
                if (imageDomain.byteArray.contentEquals(imageModel.bytes)) {
                    imageDomain.id = imageModel.id
                }
            }
        }

        val response = webClient.save(product = product, images = productImages)

        val objectSynchronized = response.getObjectSynchronized()
        product.synchronized = objectSynchronized
        productImages.forEach { it.synchronized = objectSynchronized }

        productDAO.save(product = product)
        productImageDAO.save(images = productImages)

        return response
    }

    suspend fun updateImage(productImageDomain: ProductImageDomain): PersistenceResponse {
        val productImage = productImageDomain.run {
            ProductImage(
                id = id!!,
                active = active,
                marketId = marketId,
                synchronized = synchronized,
                bytes = byteArray,
                productId = productId,
                principal = principal
            )
        }

        val response = webClient.updateProductImage(productImage)
        productImage.synchronized = response.getObjectSynchronized()

        productImageDAO.updateImage(image = productImage)

        return response
    }

    suspend fun toggleActiveProduct(productId: String) {
        val response = webClient.toggleActiveProduct(productId)
        val synchronized = response.getObjectSynchronized()

        productDAO.toggleActiveProductAndImages(productId = productId, sync = synchronized)
    }

    suspend fun toggleActiveProductImage(imageId: String, productId: String) {
        val images = productImageDAO.findProductImagesBy(productId).toMutableList()
        val imageToToggleActive = images.find { it.id == imageId && it.active }!!
        images.remove(imageToToggleActive)

        if (imageToToggleActive.active && imageToToggleActive.principal) {
            val image = images[0]
            image.principal = true

            val response = webClient.updateProductImage(productImage = image)
            image.synchronized = response.getObjectSynchronized()
            productImageDAO.updateImage(image = image)
        }

        val response = webClient.toggleActiveProductImage(imageId)
        productImageDAO.toggleActive(id = imageId, sync = response.getObjectSynchronized())
    }

    suspend fun sync(): MarketServiceResponse {
        val response = sendProductsToRemoteDB()
        return if (response.success) updateProductsOfLocalDB() else response
    }

    private suspend fun sendProductsToRemoteDB(): MarketServiceResponse {
        val productsNotSynchronized = productDAO.findProductsNotSynchronized()
        val productImagesNotSynchronized = productImageDAO.findProductImagesNotSynchronized()
        val response = webClient.sync(productsNotSynchronized, productImagesNotSynchronized)

        if (response.success) {
            val productsSynchronized = productsNotSynchronized.map { it.copy(synchronized = true) }
            productDAO.save(productsSynchronized)

            val productImagesSynchronized = productImagesNotSynchronized.map { it.copy(synchronized = true) }
            productImageDAO.save(productImagesSynchronized)
        }

        return response
    }

    private suspend fun updateProductsOfLocalDB(): MarketServiceResponse {
        val marketId = marketDAO.findFirst().first()?.id!!

        var response = importPagingData(
            onWebServiceFind = { limit, offset ->
                webClient.findProducts(marketId = marketId, limit = limit, offset = offset)
            },
            onPersistData = productDAO::save
        )

        if (response.success) {
            response = importPagingData(
                onWebServiceFind = { limit, offset ->
                    webClient.findProductImages(marketId = marketId, limit = limit, offset = offset)
                },
                onPersistData = productImageDAO::save
            )
        }

        return response
    }
}