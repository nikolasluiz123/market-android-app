package br.com.market.core.pagination

import androidx.paging.PagingConfig

object PagingConfigUtils {

    fun defaultPagingConfig() = PagingConfig(
        pageSize = 50,
        prefetchDistance = 25,
        enablePlaceholders = false,
        initialLoadSize = 50
    )

    fun customPagingConfig(pageSize: Int) = PagingConfig(
        pageSize = pageSize,
        prefetchDistance = pageSize / 2,
        enablePlaceholders = false,
        initialLoadSize = pageSize
    )
}