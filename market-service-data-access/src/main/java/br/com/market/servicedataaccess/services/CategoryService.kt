package br.com.market.servicedataaccess.services

import br.com.market.sdo.brand.*
import br.com.market.sdo.category.CategorySDO
import br.com.market.servicedataaccess.responses.PersistenceResponse
import retrofit2.Response
import retrofit2.http.*


interface CategoryService {

    @POST("category")
    suspend fun save(@Header("Authorization") token: String, @Body categorySDO: CategorySDO): Response<PersistenceResponse>

    @POST("category/toggleActive")
    suspend fun toggleActive(@Header("Authorization") token: String, @Body categorySDO: CategorySDO): Response<PersistenceResponse>

}