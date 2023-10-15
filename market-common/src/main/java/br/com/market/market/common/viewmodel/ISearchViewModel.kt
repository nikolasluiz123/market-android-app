package br.com.market.market.common.viewmodel

import androidx.paging.PagingData
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.domain.base.BaseDomain
import kotlinx.coroutines.flow.Flow

interface ISearchViewModel<DOMAIN: BaseDomain, FILTER: BaseSearchFilter> {

    fun onSimpleFilterChange(value: String?)

    fun getDataFlow(filter: FILTER): Flow<PagingData<DOMAIN>>
}