package br.com.market.market.common.mediator

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.market.domain.base.BaseDomain
import br.com.market.localdataaccess.database.AppDatabase
import br.com.market.market.common.R
import br.com.market.models.base.BaseRemoteKeyModel
import br.com.market.sdo.base.BaseSDO
import br.com.market.servicedataaccess.responses.types.ReadResponse
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<DOMAIN : BaseDomain, KEY : BaseRemoteKeyModel, SDO : BaseSDO>(
    private val context: Context,
    private val database: AppDatabase
) : RemoteMediator<Int, DOMAIN>() {

    abstract suspend fun getDataOfService(limit: Int, offset: Int): ReadResponse<SDO>

    abstract suspend fun onLoadDataRefreshType()

    abstract suspend fun onSaveDataCache(response: ReadResponse<SDO>, remoteKeys: List<KEY>)

    abstract fun getRemoteKeysFromServiceData(ids: List<String>, prevKey: Int?, nextKey: Int?, currentPage: Int): List<KEY>

    abstract suspend fun getCreationTime(): Long?
    
    abstract suspend fun getRemoteKeyByID(id: String): KEY?
    
    open fun getCacheTimeout(): Pair<Long, TimeUnit> {
        return Pair(1L, TimeUnit.MINUTES)
    }

    override suspend fun initialize(): InitializeAction {
        val pairTimeout = getCacheTimeout()
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(pairTimeout.first, pairTimeout.second)

        return if (System.currentTimeMillis() - (getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, DOMAIN>): MediatorResult {
        return try {
            val page: Int = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 0
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val limit = state.config.pageSize
            val offset = page * limit
            val response = getDataOfService(limit, offset)
            val endOfPaginationReached = response.values.isEmpty()

            if (response.success) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        onLoadDataRefreshType()
                    }

                    val prevKey = if (page > 1) page - 1 else null
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val remoteKeys = getRemoteKeysFromServiceData(
                        ids = response.values.map { it.localId },
                        prevKey = prevKey,
                        nextKey = nextKey,
                        currentPage = page
                    )

                    onSaveDataCache(response, remoteKeys)
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: ConnectException) {
            val ex = Exception(context.getString(R.string.message_connect_exception), e)
            ex.printStackTrace()
            
            MediatorResult.Error(ex)
        } catch (e: SocketTimeoutException) {
            val ex = Exception(context.getString(R.string.message_socket_timeout_exception), e)
            ex.printStackTrace()

            MediatorResult.Error(ex)
        } catch (e: Exception) {
            val ex = Exception(context.getString(R.string.message_load_data_exception), e)
            ex.printStackTrace()

            MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, DOMAIN>): KEY? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                getRemoteKeyByID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DOMAIN>): KEY? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { domain ->
            getRemoteKeyByID(domain.id!!)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, DOMAIN>): KEY? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { domain ->
            getRemoteKeyByID(domain.id!!)
        }
    }
}