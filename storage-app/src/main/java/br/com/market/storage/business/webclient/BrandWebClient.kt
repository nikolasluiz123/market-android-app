package br.com.market.storage.business.webclient

import android.content.Context
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.models.ProductBrand
import br.com.market.storage.business.sdo.brand.DeleteBrandSDO
import br.com.market.storage.business.sdo.brand.NewBrandSDO
import br.com.market.storage.business.sdo.brand.UpdateBrandSDO
import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.sdo.product.UpdateProductSDO
import br.com.market.storage.business.services.BrandService
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import br.com.market.storage.business.webclient.extensions.getPersistenceResponseBody
import br.com.market.storage.business.webclient.extensions.getResponseBody
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BrandWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val brandService: BrandService
) : BaseWebClient(context) {

    suspend fun saveBrand(productId: Long, brand: Brand, productBrand: ProductBrand): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val newBrandSDO = NewBrandSDO(localProductId = productId, localBrandId = brand.id, name = brand.name, count = productBrand.count)
                brandService.saveBrand(getToken(), newBrandSDO).getPersistenceResponseBody()
            }
        )
    }

    suspend fun updateBrand(brand: Brand, productBrand: ProductBrand): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val updateBrandSDO = UpdateBrandSDO(brand.id!!, brand.name, productBrand.count)
                brandService.updateBrand(getToken(), updateBrandSDO).getPersistenceResponseBody()
            }
        )
    }

    suspend fun deleteBrand(localBrandId: Long): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val deleteBrandSDO = DeleteBrandSDO(localBrandId)
                brandService.deleteBrand(getToken(), deleteBrandSDO).getResponseBody()
            }
        )
    }

    suspend fun syncBrands(activeBrandsToSync: List<Brand>, activeProductsBrandsToSync: List<ProductBrand>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val brandsDTOs = mutableListOf<NewBrandSDO>()

                for (brand in activeBrandsToSync) {
                    val productBrand = activeProductsBrandsToSync.find { it.brandId == brand.id }!!
                    brandsDTOs.add(NewBrandSDO(productBrand.productId, brand.id, brand.name, productBrand.count))
                }

                brandService.syncBrands(getToken(), brandsDTOs).getResponseBody()
            }
        )
    }

    suspend fun deleteBrands(
        inactiveAndNotSynchronizedBrands: List<Brand>,
    ): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val brandsDTOs = inactiveAndNotSynchronizedBrands.map { DeleteBrandSDO(it.id!!) }
                brandService.deleteBrands(getToken(), brandsDTOs).getResponseBody()
            }
        )
    }

}