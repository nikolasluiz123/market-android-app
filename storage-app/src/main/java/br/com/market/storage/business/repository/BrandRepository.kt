package br.com.market.storage.business.repository

import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.ProductBrand
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import br.com.market.storage.business.webclient.BrandWebClient
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductBrandDomain
import br.com.market.storage.utils.TransformClassHelper
import kotlinx.coroutines.flow.Flow
import java.net.HttpURLConnection
import javax.inject.Inject

class BrandRepository @Inject constructor(
    private val brandWebClient: BrandWebClient,
    private val brandDAO: BrandDAO
) {

    suspend fun save(productId: Long, brandDomain: BrandDomain): PersistenceResponse {
        val brand = Brand()
        TransformClassHelper.domainToModel(domain = brandDomain, model = brand)

        val brandId = brandDAO.saveBrand(brand)

        val productBrand = if (brand.id != null) {
            val pb = brandDAO.findProductBrandByBrandId(brand.id!!)
            pb.copy(count = brandDomain.count)
        } else {
            ProductBrand(
                productId = productId,
                brandId = brandId,
                count = brandDomain.count
            )
        }

        brandDAO.saveProductBrand(productBrand)

        var response = if (brand.id == null) {
            brand.id = brandId
            brandWebClient.saveBrand(productId = productId, brand = brand, productBrand = productBrand)
        } else {
            brandWebClient.updateBrand(brand = brand, productBrand = productBrand)
        }

        brand.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE
        productBrand.synchronized = response.success && response.code != HttpURLConnection.HTTP_UNAVAILABLE

        brandDAO.saveBrand(brand)
        brandDAO.saveProductBrand(productBrand)

        /**
         * Fazendo isso para que quando não conseguir se conectar com o servidor retorne sucesso
         * por conta da persistência local
         */
        response.success = response.success || response.code == HttpURLConnection.HTTP_UNAVAILABLE

        response = response.copy(idLocal = brandId, idRemote = response.idRemote)

        return response
    }

    suspend fun deleteBrand(localBrandId: Long): MarketServiceResponse {
        brandDAO.inactivateBrandAndReferences(localBrandId)

        val response = brandWebClient.deleteBrand(localBrandId)

        if (response.success) {
            brandDAO.deleteBrandAndReferences(localBrandId)
        }

        // Fazendo isso para que quando não conseguir se conectar com o servidor retorne sucesso
        // por conta da inativação local
        response.success = response.success || response.code == HttpURLConnection.HTTP_UNAVAILABLE

        return response
    }

    suspend fun syncBrands(): MarketServiceResponse {
        val activeBrandsToSync = brandDAO.findAllActiveBrandsNotSynchronized()
        val activeProductsBrandsToSync = brandDAO.findAllActiveProductsBrandsNotSynchronized()

        val syncResponse = brandWebClient.syncBrands(activeBrandsToSync, activeProductsBrandsToSync)

        if (syncResponse.success) {
            activeBrandsToSync.forEach {
                val brand = it.copy(synchronized = true)
                brandDAO.saveBrand(brand)
            }

            activeProductsBrandsToSync.forEach {
                val productBrand = it.copy(synchronized = true)
                brandDAO.saveProductBrand(productBrand)
            }

            val inactiveAndNotSynchronizedBrands = brandDAO.findAllInactiveAndNotSynchronizedBrands()

            val deleteResponse = brandWebClient.deleteBrands(inactiveAndNotSynchronizedBrands)

            if (deleteResponse.success) {
                inactiveAndNotSynchronizedBrands.forEach {
                    brandDAO.deleteBrandAndReferences(it.id!!)
                }
            }

            return deleteResponse

        } else {
            return syncResponse
        }
    }

    fun findProductBrandsByProductId(productId: Long?): Flow<List<ProductBrandDomain>> {
        return brandDAO.findProductBrandsByProductId(productId)
    }

}