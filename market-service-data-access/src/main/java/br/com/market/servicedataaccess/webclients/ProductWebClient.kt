package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.sdo.ProductBodySDO
import br.com.market.sdo.ProductImageSDO
import br.com.market.sdo.ProductSDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.IProductService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IProductService
) : BaseWebClient(context) {

    suspend fun save(product: Product, images: List<ProductImage>): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val productSDO = ProductSDO(
                    name = product.name,
                    price = product.price,
                    quantity = product.quantity,
                    quantityUnit = product.quantityUnit,
                    categoryBrandLocalId = product.categoryBrandId,
                    active = product.active,
                    localId = product.id,
                    marketId = product.marketId
                )

                val imagesSDO = images.map {
                    ProductImageSDO(
                        localId = it.id,
                        active = it.active,
                        bytes = it.bytes,
                        imageUrl = it.imageUrl,
                        productLocalId = it.productId,
                        principal = it.principal,
                        marketId = it.marketId
                    )
                }

                service.save(getToken(), ProductBodySDO(productSDO, imagesSDO)).getPersistenceResponseBody()
            }
        )
    }

    suspend fun updateProductImage(productImage: ProductImage): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val productImageSDO = productImage.run {
                    ProductImageSDO(
                        localId = id,
                        active = active,
                        bytes = bytes,
                        imageUrl = imageUrl,
                        productLocalId = productId,
                        principal = principal,
                        marketId = marketId
                    )
                }

                service.updateProductImage(getToken(), productImageSDO).getPersistenceResponseBody()
            }
        )
    }

    suspend fun toggleActiveProduct(productLocalId: String): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                service.toggleActiveProduct(getToken(), productLocalId).getPersistenceResponseBody()
            }
        )
    }

    suspend fun toggleActiveProductImage(productImageLocalId: String): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                service.toggleActiveProductImage(getToken(), productImageLocalId).getPersistenceResponseBody()
            }
        )
    }

    suspend fun sync(products: List<Product>, images: List<ProductImage>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val productSDOs = products.map {
                    ProductSDO(
                        localId = it.id,
                        active = it.active,
                        name = it.name,
                        price = it.price,
                        quantityUnit = it.quantityUnit,
                        quantity = it.quantity,
                        categoryBrandLocalId = it.categoryBrandId,
                        marketId = it.marketId
                    )
                }

                val productImageSDOs = images.map {
                    ProductImageSDO(
                        localId = it.id,
                        active = it.active,
                        bytes = it.bytes,
                        imageUrl = it.imageUrl,
                        productLocalId = it.productId,
                        principal = it.principal,
                        marketId = it.marketId
                    )
                }

                val syncList = mutableListOf<ProductBodySDO>()

                for (productSDO in productSDOs) {
                    val productImages = productImageSDOs.filter { it.productLocalId == productSDO.localId }
                    syncList.add(ProductBodySDO(productSDO, productImages))
                }

                service.sync(getToken(), syncList).getResponseBody()
            }
        )
    }

    suspend fun findProducts(marketId: Long, limit: Int? = null, offset: Int? = null): ReadResponse<Product> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findProductSDOs(
                    token = getToken(),
                    marketId = marketId,
                    limit = limit,
                    offset = offset
                ).getReadResponseBody()

                val products = response.values.map {
                    Product(
                        id = it.localId,
                        name = it.name!!,
                        price = it.price!!,
                        quantity = it.quantity!!,
                        quantityUnit = it.quantityUnit,
                        categoryBrandId = it.categoryBrandLocalId,
                        synchronized = true,
                        active = it.active,
                        marketId = it.marketId
                    )
                }

                ReadResponse(values = products, code = response.code, success = response.success, error = response.error)
            }
        )
    }

    suspend fun findProductImages(marketId: Long, limit: Int? = null, offset: Int? = null): ReadResponse<ProductImage> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findProductImageSDOs(
                    token = getToken(),
                    marketId = marketId,
                    limit = limit,
                    offset = offset
                ).getReadResponseBody()

                val images = response.values.map {
                    ProductImage(
                        id = it.localId,
                        synchronized = true,
                        active = it.active,
                        bytes = it.bytes,
                        imageUrl = it.imageUrl,
                        productId = it.productLocalId,
                        principal = it.principal,
                        marketId = it.marketId
                    )
                }

                ReadResponse(values = images, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}