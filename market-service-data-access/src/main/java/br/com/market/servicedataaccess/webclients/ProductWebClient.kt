package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.sdo.product.ProductBodySDO
import br.com.market.sdo.product.ProductImageSDO
import br.com.market.sdo.product.ProductSDO
import br.com.market.servicedataaccess.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.PersistenceResponse
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
                    localId = product.id
                )

                val imagesSDO = images.map {
                    ProductImageSDO(
                        localId = it.id,
                        active = it.active,
                        bytes = it.bytes,
                        imageUrl = it.imageUrl,
                        productLocalId = it.productId
                    )
                }

                service.save(getToken(), ProductBodySDO(productSDO, imagesSDO)).getPersistenceResponseBody()
            }
        )
    }
}