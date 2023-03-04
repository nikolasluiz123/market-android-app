package br.com.market.storage.business.services

import br.com.market.storage.business.sdo.brand.BrandViewSDO
import br.com.market.storage.business.sdo.brand.UpdateStorageSDO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface BrandService {

    @GET("brand/{productId}")
    suspend fun findProductBrands(@Path("productId") productId: Long): List<BrandViewSDO>

    @POST("brand/add")
    suspend fun sumStorageCount(@Header("Authorization") token: String, storageDTO: UpdateStorageSDO): Response<Void>

    @POST("brand/subtract")
    suspend fun subtractStorageCount(@Header("Authorization") token: String, storageDTO: UpdateStorageSDO): Response<Void>

}