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
import br.com.market.localdataaccess.dao.remotekeys.ProductRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.lov.ProductRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.sdo.ProductAndReferencesSDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import br.com.market.servicedataaccess.services.params.ProductServiceSearchParams
import br.com.market.servicedataaccess.webclients.ProductWebClient
import kotlinx.coroutines.flow.first
import java.net.HttpURLConnection
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val productRemoteKeyDAO: ProductRemoteKeysDAO,
    private val productDAO: ProductDAO,
    private val marketDAO: MarketDAO,
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
                webClient = webClient,
                params = ProductServiceSearchParams(
                    categoryId = filters.categoryId,
                    brandId = filters.brandId,
                    quickFilter = filters.quickFilter
                )
            )
        )
    }

    suspend fun findProductByLocalId(productId: String): SingleValueResponse<ProductAndReferencesSDO> {
        return webClient.findProductByLocalId(productId)
    }

    suspend fun findProductImageDomain(productImageId: String): ProductImageDomain? {
        return productDAO.findProductImageBy(productImageId)?.run {
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

    suspend fun save(domain: ProductDomain, categoryBrandId: String): PersistenceResponse {
        return if (domain.id != null) {
            editProduct(domain)
        } else {
            createProduct(domain, categoryBrandId)
        }
    }

    private suspend fun createProduct(domain: ProductDomain, categoryBrandId: String): PersistenceResponse {
        val marketId = marketDAO.findFirst().first()?.id!!

        val product = domain.run {
            Product(
                name = name!!,
                price = price!!,
                quantityUnit = quantityUnit,
                quantity = quantity!!,
                categoryBrandId = categoryBrandId,
                marketId = marketId
            )
        }

        val images = domain.images.map {
            ProductImage(
                bytes = it.byteArray,
                productId = product.id,
                principal = it.principal,
                marketId = marketId
            )
        }

        domain.id = product.id

        return saveProduct(product, images)
    }

    private suspend fun saveProduct(product: Product, images: List<ProductImage>): PersistenceResponse {
        val response = webClient.save(product = product, images = images)

        return if (response.success) {
            productDAO.saveProductAndImages(product, images)
            PersistenceResponse(code = HttpURLConnection.HTTP_OK, success = true)
        } else {
            PersistenceResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, error = response.error)
        }
    }

    private suspend fun editProduct(domain: ProductDomain): PersistenceResponse {
        val response = webClient.findProductByLocalId(productId = domain.id!!)

        return if (response.success) {
            val product = getProductWithUpdatedInfo(response, domain)
            val images = getImagesWithUpdatedInfo(response, domain)

            saveProduct(product, images)
        } else {
            PersistenceResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, error = response.error)
        }
    }

    private fun getProductWithUpdatedInfo(response: SingleValueResponse<ProductAndReferencesSDO>, domain: ProductDomain): Product {
        return response.value!!.run {
            Product(
                id = product.localId,
                name = domain.name!!,
                price = domain.price!!,
                quantity = domain.quantity!!,
                quantityUnit = domain.quantityUnit,
                categoryBrandId = product.categoryBrandLocalId,
                marketId = product.marketId,
                active = product.active
            )
        }
    }

    private fun getImagesWithUpdatedInfo(response: SingleValueResponse<ProductAndReferencesSDO>, domain: ProductDomain): List<ProductImage> {
        return response.value!!.productImages.map { sdo ->
            val image = domain.images.first { image -> image.id == sdo.localId }

            ProductImage(
                id = sdo.localId,
                bytes = image.byteArray,
                productId = sdo.productLocalId,
                principal = image.principal,
                marketId = sdo.marketId
            )
        }
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

        if (response.success) {
            productDAO.updateImage(image = productImage)
        }

        return response
    }

    suspend fun toggleActiveProduct(productId: String): PersistenceResponse {
        val response = webClient.toggleActiveProduct(productId)

        if (response.success) {
            productDAO.toggleActiveProductAndImages(productId = productId)
        }

        return response
    }

    suspend fun toggleActiveProductImage(imageId: String, productId: String) {
        val response = webClient.toggleActiveProductImage(productId, imageId)

        if (response.success) {
            productDAO.toggleActive(imageId, productId)
        }
    }

}