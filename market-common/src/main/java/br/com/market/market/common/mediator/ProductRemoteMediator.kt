package br.com.market.market.common.mediator

import android.content.Context
import br.com.market.domain.ProductImageReadDomain
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.remotekeys.ProductRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.models.Product
import br.com.market.models.ProductImage
import br.com.market.models.keys.ProductRemoteKeys
import br.com.market.sdo.ProductAndReferencesSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.params.ProductServiceSearchParams
import br.com.market.servicedataaccess.webclients.ProductWebClient

class ProductRemoteMediator(
    database: AppDatabase,
    context: Context,
    private val remoteKeysDAO: ProductRemoteKeysDAO,
    private val productDAO: ProductDAO,
    private val webClient: ProductWebClient,
    private val params: ProductServiceSearchParams

    ): BaseRemoteMediator<ProductImageReadDomain, ProductRemoteKeys, ProductAndReferencesSDO>(context, database) {
    override suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<ProductAndReferencesSDO> {
        params.limit = limit
        params.offset = offset
        
        return webClient.getListProducts(params)
    }

    override suspend fun onLoadDataRefreshType() {
        remoteKeysDAO.clearRemoteKeys()
        productDAO.clearAll()
    }

    override suspend fun onSaveDataCache(response: ReadResponse<ProductAndReferencesSDO>, remoteKeys: List<ProductRemoteKeys>) {
        remoteKeysDAO.insertAll(remoteKeys)

        val products = getProductsFrom(response)
        val images = getProductsImagesFrom(response)

        productDAO.saveProductsAndImages(products, images)
    }
    override fun getRemoteKeysFromServiceData(ids: List<String>, prevKey: Int?, nextKey: Int?, currentPage: Int): List<ProductRemoteKeys> {
        return ids.map { id ->
            ProductRemoteKeys(id = id, prevKey = prevKey, currentPage = currentPage, nextKey = nextKey)
        }
    }

    override suspend fun getCreationTime(): Long? {
        return remoteKeysDAO.getCreationTime()
    }

    override suspend fun getRemoteKeyByID(id: String): ProductRemoteKeys? {
        return remoteKeysDAO.getRemoteKeyByID(id)
    }

    override fun getEntityLocalId(sdo: ProductAndReferencesSDO): String {
        return sdo.product.localId
    }

    private fun getProductsFrom(response: ReadResponse<ProductAndReferencesSDO>): List<Product> = response.values.map {
        Product(
            id = it.product.localId,
            name = it.product.name!!,
            price = it.product.price!!,
            quantity = it.product.quantity!!,
            quantityUnit = it.product.quantityUnit,
            categoryBrandId = it.product.categoryBrandLocalId,
            marketId = it.product.marketId
        )
    }

    private fun getProductsImagesFrom(response: ReadResponse<ProductAndReferencesSDO>): List<ProductImage> = response.values.map {
        it.productImages.first().run {
            ProductImage(
                id = localId,
                bytes = bytes,
                imageUrl = imageUrl,
                productId = productLocalId,
                principal = principal,
                marketId = marketId
            )
        }
    }
}