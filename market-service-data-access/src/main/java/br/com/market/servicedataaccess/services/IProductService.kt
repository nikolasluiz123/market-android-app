package br.com.market.servicedataaccess.services

import br.com.market.sdo.ProductBodySDO
import br.com.market.sdo.ProductClientSDO
import br.com.market.sdo.ProductImageSDO
import br.com.market.sdo.ProductSDO
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.*


interface IProductService {

    @POST("product")
    suspend fun save(@Header("Authorization") token: String, @Body productBodySDO: ProductBodySDO): Response<PersistenceResponse>

    @POST("product/image")
    suspend fun updateProductImage(@Header("Authorization") token: String, @Body productImageSDO: ProductImageSDO): Response<PersistenceResponse>

    @POST("product/toggleActive")
    suspend fun toggleActiveProduct(@Header("Authorization") token: String, @Query("productLocalId") productLocalId: String): Response<PersistenceResponse>

    @POST("product/image/toggleActive")
    suspend fun toggleActiveProductImage(@Header("Authorization") token: String, @Query("productImageLocalId") productImageLocalId: String): Response<PersistenceResponse>

    @POST("product/sync")
    suspend fun sync(@Header("Authorization") token: String, @Body productBodySDOs: List<ProductBodySDO>): Response<MarketServiceResponse>

    @GET("product")
    suspend fun findProductSDOs(
        @Header("Authorization") token: String,
        @Query("marketId") marketId: Long,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<ReadResponse<ProductSDO>>

    @GET("product/images")
    suspend fun findProductImageSDOs(
        @Header("Authorization") token: String,
        @Query("marketId") marketId: Long,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<ReadResponse<ProductImageSDO>>

    @GET("product/client")
    suspend fun findProducts(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ReadResponse<ProductClientSDO>>
}