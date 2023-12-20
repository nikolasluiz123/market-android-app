package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.StorageOperationHistory
import br.com.market.sdo.StorageOperationHistorySDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
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
                        userId = userId,
                        marketId = marketId
                    )

                    service.save(getToken(), storageSDO).getPersistenceResponseBody()
                }
            }
        )
    }

    suspend fun inactivate(localId: String): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                service.inactivate(getToken(), localId).getPersistenceResponseBody()
            }
        )
    }
}