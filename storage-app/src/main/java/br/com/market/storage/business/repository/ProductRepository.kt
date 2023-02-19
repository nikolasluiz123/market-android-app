package br.com.market.storage.business.repository

import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.dao.ProductDAO
import br.com.market.storage.business.models.Brand
import br.com.market.storage.business.models.Product
import br.com.market.storage.business.webclient.ProductWebClient
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDAO: ProductDAO,
    private val brandDAO: BrandDAO,
    private val productWebClient: ProductWebClient
) {

    suspend fun sincronize() {
        val productListInativated = productDAO.findAllProductsInativated().first()
        productListInativated.forEach { deleteProduct(it) }

        val productListNotSincronized = productDAO.findAllProductsNotSincronized().first()

        val productBrandsListNotSincronized = productDAO.findAllProductsBrandsNotSincronized().first()

        val brandsListNotSincronized = brandDAO.findAllBrandsNotSincronized().first()
    }

    suspend fun save(product: Product, brands: List<Brand>) {
        saveProductBrandsLocal(product, brands)

        val response = productWebClient.saveProduct(product, brands)

        if (response.isSuccessful) {
            updateProductBrandsLocalSincronized(product, brands)
        }
    }

    suspend fun update(product: Product, brands: List<Brand>) {
        saveProductBrandsLocal(product, brands)

        val response = productWebClient.updateProduct(product, brands)

        if (response.isSuccessful) {
            updateProductBrandsLocalSincronized(product, brands)
        }
    }

    private suspend fun updateProductBrandsLocalSincronized(product: Product, brands: List<Brand>) {
        val productSincronized = product.copy(sincronized = true)
        val brandsSincronized = brands.map { it.copy(sincronized = true) }
        saveProductBrandsLocal(productSincronized, brandsSincronized)
    }

    private suspend fun saveProductBrandsLocal(product: Product, brands: List<Brand>) {
        productDAO.saveProduct(product)
        brandDAO.saveBrands(brands)
    }

    suspend fun deleteProduct(product: Product) {
        productDAO.inativateProductAndReferences(product.id!!)

        val response = productWebClient.deleteProduct(product)

        if (response.isSuccessful) {
            productDAO.deleteProductAndReferences(product.id)
        }
    }
}