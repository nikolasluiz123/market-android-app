package br.com.market.storage.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.dao.CategoryDAO

class CategoryPagingSource(private val categoryDAO: CategoryDAO) : PagingSource<Int, CategoryDomain>() {

    override fun getRefreshKey(state: PagingState<Int, CategoryDomain>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryDomain> {
        val pageNumber = params.key ?: 0

        val data = if (pageNumber == 0) {
            categoryDAO.findCategories(pageNumber, params.loadSize)
        } else {
            categoryDAO.findCategories(params.loadSize * pageNumber, params.loadSize)
        }

        val nextKey = if (data.size < params.loadSize) null else pageNumber + 1

        return LoadResult.Page(
            data = data,
            prevKey = if (pageNumber == 0) null else pageNumber - 1,
            nextKey = nextKey
        )
    }
}