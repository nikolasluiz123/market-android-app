package br.com.market.storage.business.services

import br.com.market.storage.business.sdo.brand.DeleteBrandSDO
import br.com.market.storage.business.sdo.brand.NewBrandSDO
import br.com.market.storage.business.sdo.brand.UpdateBrandSDO
import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface BrandService {

    @POST("brand")
    suspend fun saveBrand(@Header("Authorization") token: String, @Body brandSDO: NewBrandSDO): Response<PersistenceResponse>

    @PUT("brand")
    suspend fun updateBrand(@Header("Authorization") token: String, @Body brandSDO: UpdateBrandSDO): Response<PersistenceResponse>

    @POST("brand/delete")
    suspend fun deleteBrand(@Header("Authorization") token: String, @Body brandSDO: DeleteBrandSDO): Response<MarketServiceResponse>

    @POST("brand/synchronize")
    suspend fun syncBrands(@Header("Authorization") token: String, @Body brandsDTOList: List<NewBrandSDO>): Response<MarketServiceResponse>

    @POST("brand/synchronize/delete")
    suspend fun deleteBrands(@Header("Authorization") token: String, @Body brandsDTOList: List<DeleteBrandSDO>): Response<MarketServiceResponse>

}