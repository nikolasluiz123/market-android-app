package br.com.market.storage.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.filter.ProductFilter
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.ProductDomain
import br.com.market.domain.ProductImageDomain
import br.com.market.domain.ProductImageReadDomain
import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.ProductImageDAO
import br.com.market.localdataaccess.dao.remotekeys.ProductRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.lov.ProductRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.services.params.ProductServiceSearchParams
import br.com.market.servicedataaccess.webclients.ProductWebClient
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val productRemoteKeyDAO: ProductRemoteKeysDAO,
    private val productDAO: ProductDAO,
    private val marketDAO: MarketDAO,
    private val productImageDAO: ProductImageDAO,
    private val brandDAO: BrandDAO,
    private val webClient: ProductWebClient
): BaseRepository(), IPagedRemoteSearchRepository<ProductFilter, ProductImageReadDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(context: Context, filters: ProductFilter): Pager<Int, ProductImageReadDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { productDAO.findProducts(filters) },
            remoteMediator = ProductRemoteMediator(
                database = appDatabase,
                context = context,
                remoteKeysDAO = productRemoteKeyDAO,
                productDAO = productDAO,
                productImageDAO = productImageDAO,
                webClient = webClient,
                params = ProductServiceSearchParams(
                    categoryId = filters.categoryId,
                    brandId = filters.brandId,
                    quickFilter = filters.quickFilter
                )
            )
        )
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

}