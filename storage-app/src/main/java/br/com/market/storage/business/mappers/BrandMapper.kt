package br.com.market.storage.business.mappers

import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.sdo.brand.BrandViewSDO
import br.com.market.storage.business.sdo.brand.NewBrandSDO
import br.com.market.storage.business.sdo.brand.UpdateBrandSDO
import br.com.market.storage.business.dto.ProductBrandDTO
import br.com.market.storage.ui.domains.BrandDomain

object BrandMapper {

    fun toProductBrandDTO(brandViewSDO: BrandViewSDO): ProductBrandDTO {
        return ProductBrandDTO(id = brandViewSDO.id, name = brandViewSDO.name, count = brandViewSDO.count)
    }

    fun toNewBrandSDO(brand: Brand): NewBrandSDO {
        return NewBrandSDO(id = brand.id, name = brand.name)
    }

    fun toUpdateBrandSDO(brand: Brand): UpdateBrandSDO {
        return UpdateBrandSDO(id = brand.id, name = brand.name)
    }
}