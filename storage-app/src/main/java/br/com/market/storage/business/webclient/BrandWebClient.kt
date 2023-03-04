package br.com.market.storage.business.webclient

import br.com.market.storage.business.services.BrandService
import br.com.market.storage.business.dto.ProductBrandDTO
import br.com.market.storage.business.mappers.BrandMapper
import br.com.market.storage.business.sdo.brand.UpdateStorageSDO
import retrofit2.Response
import javax.inject.Inject

class BrandWebClient @Inject constructor(private val brandService: BrandService) {

//    suspend fun findProductBrands(productId: Long): List<ProductBrandDTO> {
//        return brandService.findProductBrands(productId).map(BrandMapper::toProductBrandDTO)
//    }

//    suspend fun sumStorageCount(productId: Long, brandId: Long, count: Int): Response<Void> {
//        return brandService.sumStorageCount(UpdateStorageSDO(productId, brandId, count))
//    }
//
//    suspend fun subtractStorageCount(productId: Long, brandId: Long, count: Int): Response<Void> {
//        return brandService.subtractStorageCount(UpdateStorageSDO(productId, brandId, count))
//    }
}