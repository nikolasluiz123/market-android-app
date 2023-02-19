package br.com.market.storage.business.mappers

import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.sdo.product.DeleteProductSDO
import br.com.market.storage.business.sdo.product.NewProductSDO
import br.com.market.storage.business.sdo.product.ProductViewSDO
import br.com.market.storage.business.sdo.product.UpdateProductSDO

object ProductMapper {

    fun toNewProductSDO(product: Product, brands: List<Brand>): NewProductSDO {
        return NewProductSDO(
            id = product.id,
            name = product.name,
            imageUrl = product.imageUrl,
            brands = brands.map(BrandMapper::toNewBrandSDO)
        )
    }

    fun toUpdateProductSDO(product: Product, brands: List<Brand>): UpdateProductSDO {
        return UpdateProductSDO(
            id = product.id,
            name = product.name,
            imageUrl = product.imageUrl,
            brands = brands.map(BrandMapper::toUpdateBrandSDO)
        )
    }

    fun toDeleteProductSDO(product: Product): DeleteProductSDO {
        return DeleteProductSDO(id = product.id!!)
    }

    fun toProductModel(productViewSDO: ProductViewSDO): Product {
        return Product(
            id = productViewSDO.id,
            name = productViewSDO.name,
            imageUrl = productViewSDO.imageUrl
        )
    }
}