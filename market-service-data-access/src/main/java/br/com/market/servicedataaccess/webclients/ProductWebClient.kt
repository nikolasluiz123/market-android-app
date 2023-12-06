package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.sdo.ProductAndReferencesSDO
import br.com.market.sdo.ProductClientSDO
import br.com.market.sdo.ProductImageSDO
import br.com.market.sdo.ProductSDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.IProductService
import br.com.market.servicedataaccess.services.params.ProductServiceSearchParams
import com.google.gson.Gson
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

                service.save(getToken(), ProductAndReferencesSDO(productSDO, imagesSDO)).getPersistenceResponseBody()
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

    suspend fun findProductsForSell(simpleFilter: String?, limit: Int, offset: Int): ReadResponse<ProductClientSDO> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                service.findProducts(getToken(), simpleFilter, limit, offset).getReadResponseBody()
            }
        )
    }

    suspend fun getListProducts(params: ProductServiceSearchParams): ReadResponse<ProductAndReferencesSDO> {
        val jsonParams = Gson().toJson(params)

        return readServiceErrorHandlingBlock(
            codeBlock = {
                service.getListProducts(getToken(), jsonParams).getReadResponseBody()
            }
        )
    }
}