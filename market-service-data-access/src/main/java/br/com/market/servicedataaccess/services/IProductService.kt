package br.com.market.servicedataaccess.services

import br.com.market.sdo.ProductAndReferencesSDO
import br.com.market.sdo.ProductClientSDO
import br.com.market.sdo.ProductImageSDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import retrofit2.Response
import retrofit2.http.*


interface IProductService {

    @POST("product")
    suspend fun save(@Header("Authorization") token: String, @Body productAndReferencesSDO: ProductAndReferencesSDO): Response<PersistenceResponse>

    @POST("product/image")
    suspend fun updateProductImage(@Header("Authorization") token: String, @Body productImageSDO: ProductImageSDO): Response<PersistenceResponse>

    @POST("product/toggleActive")
    suspend fun toggleActiveProduct(
        @Header("Authorization") token: String,
        @Query("productLocalId") productLocalId: String
    ): Response<PersistenceResponse>

    @POST("product/image/toggleActive")
    suspend fun toggleActiveProductImage(
        @Header("Authorization") token: String,
        @Query("productId") productId: String,
        @Query("imageId") imageId: String
    ): Response<PersistenceResponse>

    @GET("product/client")
    suspend fun findProducts(
        @Header("Authorization") token: String,
        @Query("simpleFilter") simpleFilter: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ReadResponse<ProductClientSDO>>

    @GET("product")
    suspend fun getListProducts(
        @Header("Authorization") token: String,
        @Query("jsonParams") jsonParams: String
    ): Response<ReadResponse<ProductAndReferencesSDO>>

    @GET("product/{productId}")
    suspend fun findProductByLocalId(
        @Header("Authorization") token: String,
        @Query("productId") productId: String
    ): Response<SingleValueResponse<ProductAndReferencesSDO>>
}