package br.com.market.storage.business.repository

import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.mappers.BrandMapper
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.ProductBrand
import br.com.market.storage.business.webclient.BrandWebClient
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductBrandDomain
import br.com.market.storage.utils.TransformClassHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BrandRepository @Inject constructor(
    private val brandDAO: BrandDAO
) {
    suspend fun saveBrand(productId: Long, brandDomain: BrandDomain) {
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
    }

    fun findProductBrandsByProductId(productId: Long?): Flow<List<ProductBrandDomain>> {
        return brandDAO.findProductBrandsByProductId(productId)
    }

}