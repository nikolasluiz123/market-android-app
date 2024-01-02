package br.com.market.storage.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.StorageOperationHistoryDomain
import br.com.market.domain.StorageOperationHistoryReadDomain
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.StorageOperationsHistoryDAO
import br.com.market.core.filter.MovementFilters
import br.com.market.localdataaccess.dao.remotekeys.StorageOperationsHistoryRemoteKeysDAO
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.mediator.StorageOperationsHistoryRemoteMediator
import br.com.market.market.common.repository.BaseRepository
import br.com.market.market.common.repository.IPagedRemoteSearchRepository
import br.com.market.models.StorageOperationHistory
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.services.params.StorageOperationsHistoryServiceSearchParams
import br.com.market.servicedataaccess.webclients.StorageOperationsHistoryWebClient
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StorageOperationsHistoryRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val storageDao: StorageOperationsHistoryDAO,
    private val marketDAO: MarketDAO,
    private val webClient: StorageOperationsHistoryWebClient,
    private val remoteKeysDAO: StorageOperationsHistoryRemoteKeysDAO
): BaseRepository(), IPagedRemoteSearchRepository<MovementFilters, StorageOperationHistoryReadDomain> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConfiguredPager(context: Context, filters: MovementFilters): Pager<Int, StorageOperationHistoryReadDomain> {
        return Pager(
            config = PagingConfigUtils.customConfig(20),
            pagingSourceFactory = { storageDao.findStorageOperationsHistory(filters) },
            remoteMediator = StorageOperationsHistoryRemoteMediator(
                database = appDatabase,
                context = context,
                params = StorageOperationsHistoryServiceSearchParams(filters = filters),
                webClient = webClient,
                remoteKeyDAO = remoteKeysDAO,
                storageDAO = storageDao
            )
        )
    }

    suspend fun findStorageOperationHistoryDomainById(id: String): StorageOperationHistoryDomain {
        return storageDao.findById(id).run {
            StorageOperationHistoryDomain(
                id = id,
                active = active,
                synchronized = synchronized,
                productId = productId,
                quantity = quantity!!,
                dateRealization = dateRealization,
                datePrevision = datePrevision,
                operationType = operationType,
                description = description,
                userId = userId
            )
        }
    }

    suspend fun save(domain: StorageOperationHistoryDomain): PersistenceResponse {
        val storageOperationHistory = if (domain.id != null) {
            storageDao.findById(domain.id!!).copy(
                productId = domain.productId,
                dateRealization = domain.dateRealization,
                datePrevision = domain.datePrevision,
                operationType = domain.operationType,
                description = domain.description,
                userId = domain.userId,
                synchronized = domain.synchronized,
                active = domain.active,
                quantity = domain.quantity
            )
        } else {
            val marketId = marketDAO.findFirst().first()?.id!!

            StorageOperationHistory(
                productId = domain.productId,
                dateRealization = domain.dateRealization,
                datePrevision = domain.datePrevision,
                operationType = domain.operationType,
                description = domain.description,
                userId = domain.userId,
                synchronized = domain.synchronized,
                active = domain.active,
                quantity = domain.quantity,
                marketId = marketId
            )
        }

        domain.id = storageOperationHistory.id

        val response = webClient.save(storageOperationHistory)
        storageOperationHistory.synchronized = response.getObjectSynchronized()

        storageDao.save(storageOperationHistory)

        return response
    }

    suspend fun inactivate(id: String) {
        val response = webClient.inactivate(id)
        val synchronized = response.getObjectSynchronized()

        storageDao.inactivate(id = id, sync = synchronized)
    }

    suspend fun findProductStorageQuantity(productId: String): Int {
        return storageDao.findProductStorageQuantity(productId)
    }
}