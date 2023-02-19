package br.com.market.storage.business.webclient

import br.com.market.storage.business.mappers.ProductMapper
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.sdo.product.DeleteProductSDO
import br.com.market.storage.business.services.ProductService
import retrofit2.Response
import javax.inject.Inject

class ProductWebClient @Inject constructor(private val productService: ProductService) {

    suspend fun findAllProducts(): List<Product> {
        return productService.findAllProducts().map(ProductMapper::toProductModel)
    }

    suspend fun saveProduct(product: Product, brands: List<Brand>): Response<Void> {
        val newProductSDO = ProductMapper.toNewProductSDO(product, brands)
        return productService.saveProduct(newProductSDO)
    }

    suspend fun updateProduct(product: Product, brands: List<Brand>): Response<Void> {
        val updateProductSDO = ProductMapper.toUpdateProductSDO(product, brands)
        return productService.updateProduct(updateProductSDO)
    }

    suspend fun deleteProduct(product: Product): Response<Void> {
        val deleteProductSDO = ProductMapper.toDeleteProductSDO(product)
        return productService.deleteProduct(deleteProductSDO)
    }
}