package br.com.market.servicedataaccess.services

import br.com.market.sdo.storageoperationshistory.StorageOperationHistorySDO
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
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

    @POST("storageOperationsHistory/sync")
    suspend fun sync(@Header("Authorization") token: String, @Body operationsSDO: List<StorageOperationHistorySDO>): Response<MarketServiceResponse>

    @GET("storageOperationsHistory")
    suspend fun findStorageOperationSDOs(
        @Header("Authorization") token: String,
        @Query("marketId") marketId: Long,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<ReadResponse<StorageOperationHistorySDO>>

    @POST("storageOperationsHistory/inactivate")
    suspend fun inactivate(@Header("Authorization") token: String, @Query("localId") localId: String): Response<PersistenceResponse>
}