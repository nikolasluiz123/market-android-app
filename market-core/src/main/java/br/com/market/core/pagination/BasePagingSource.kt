package br.com.market.core.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<DOMAIN: Any> : PagingSource<Int, DOMAIN>() {

    abstract suspend fun getData(offset: Int, limit: Int): List<DOMAIN>

    override fun getRefreshKey(state: PagingState<Int, DOMAIN>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DOMAIN> {
        var offset = params.key ?: 0

        if (offset > 0) {
            offset *= params.loadSize
        }

        val data = getData(offset = offset, limit = params.loadSize)

        val nextKey = if (data.size < params.loadSize) null else offset + 1

        return LoadResult.Page(
            data = data,
            prevKey = if (offset == 0) null else offset - 1,
            nextKey = nextKey
        )
    }
}