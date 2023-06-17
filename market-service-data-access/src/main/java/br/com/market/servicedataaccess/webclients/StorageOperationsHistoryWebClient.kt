package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.StorageOperationHistory
import br.com.market.sdo.storageoperationshistory.StorageOperationHistorySDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.extensions.getResponseBody
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.IStorageOperationsHistoryService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StorageOperationsHistoryWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IStorageOperationsHistoryService
) : BaseWebClient(context) {

    suspend fun save(storageOperationHistory: StorageOperationHistory): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                with(storageOperationHistory) {
                    val storageSDO = StorageOperationHistorySDO(
                        localId = id,
                        active = active,
                        productId = productId,
                        quantity = quantity!!,
                        datePrevision = datePrevision,
                        dateRealization = dateRealization,
                        operationType = operationType,
                        description = description,
                        userId = userId
                    )

                    service.save(getToken(), storageSDO).getPersistenceResponseBody()
                }
            }
        )
    }

    suspend fun sync(storageOperationsHistoryNotSynchronized: List<StorageOperationHistory>): MarketServiceResponse {
        return serviceErrorHandlingBlock(
            codeBlock = {
                val operationsSDO = storageOperationsHistoryNotSynchronized.map {
                   StorageOperationHistorySDO(
                       localId = it.id,
                       active = it.active,
                       productId = it.productId,
                       quantity = it.quantity!!,
                       dateRealization = it.dateRealization,
                       datePrevision = it.datePrevision,
                       operationType = it.operationType,
                       description = it.description,
                       userId = it.userId
                   )
                }

                service.sync(getToken(), operationsSDO).getResponseBody()
            }
        )
    }

    suspend fun findAllStorageOperationsHistory(): ReadResponse<StorageOperationHistory> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findAllStorageOperationDTOs(getToken()).getReadResponseBody()

                val operations = response.values.map {
                    StorageOperationHistory(
                        id = it.localId,
                        dateRealization = it.dateRealization,
                        datePrevision = it.datePrevision,
                        operationType = it.operationType,
                        description = it.description,
                        userId = it.userId,
                        productId = it.productId,
                        quantity = it.quantity,
                        synchronized = true,
                        active = it.active
                    )
                }

                ReadResponse(values = operations, code = response.code, success = response.success, error = response.error)
            }
        )
    }
}