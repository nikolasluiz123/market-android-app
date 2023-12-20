package br.com.market.servicedataaccess.services

import br.com.market.sdo.StorageOperationHistorySDO
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IStorageOperationsHistoryService {

    @POST("storageOperationsHistory")
    suspend fun save(@Header("Authorization") token: String, @Body storageOperationHistorySDO: StorageOperationHistorySDO): Response<PersistenceResponse>

    @POST("storageOperationsHistory/inactivate")
    suspend fun inactivate(@Header("Authorization") token: String, @Query("localId") localId: String): Response<PersistenceResponse>

    @GET("storageOperationsHistory")
    suspend fun getListStorageOperations(
        @Header("Authorization") token: String,
        @Query("jsonParams") jsonParams: String
    ): Response<ReadResponse<StorageOperationHistorySDO>>
}