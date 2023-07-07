package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.sdo.filters.ProductFiltersSDO
import br.com.market.sdo.filters.ProductImageFiltersSDO
import br.com.market.sdo.product.ProductBodySDO
import br.com.market.sdo.product.ProductImageSDO
import br.com.market.sdo.product.ProductSDO
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
                    companyId = product.companyId
                )

                val imagesSDO = images.map {
                    ProductImageSDO(
                        localId = it.id,
                        active = it.active,
                        bytes = it.bytes,
                        imageUrl = it.imageUrl,
                        productLocalId = it.productId,
                        principal = it.principal,
                        companyId = it.companyId
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
                        companyId = companyId
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
                        companyId = it.companyId
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
                        companyId = it.companyId
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

    suspend fun findAllProducts(productFiltersSDO: ProductFiltersSDO): ReadResponse<Product> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAllProductDTOs(getToken(), productFiltersSDO).getReadResponseBody()

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
                        companyId = it.companyId
                    )
                }

                ReadResponse(values = products, code = response.code, success = response.success, error = response.error)
            }
        )
    }

    suspend fun findAllProductImages(productImageFiltersSDO: ProductImageFiltersSDO): ReadResponse<ProductImage> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAllProductImageDTOs(getToken(), productImageFiltersSDO).getReadResponseBody()

                val images = response.values.map {
                    ProductImage(
                        id = it.localId,
                        synchronized = true,
                        active = it.active,
                        bytes = it.bytes,
                        imageUrl = it.imageUrl,
                        productId = it.productLocalId,
                        principal = it.principal,
                        companyId = it.companyId
                    )
                }

                ReadResponse(values = images, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}