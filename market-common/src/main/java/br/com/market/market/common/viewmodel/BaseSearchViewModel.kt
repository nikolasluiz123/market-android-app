package br.com.market.market.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.com.market.core.filter.base.BaseSearchFilter
import br.com.market.domain.base.BaseDomain
import kotlinx.coroutines.flow.Flow

abstract class BaseSearchViewModel<DOMAIN: BaseDomain, FILTER: BaseSearchFilter>: ViewModel(){

    lateinit var filter: FILTER

    abstract fun onSimpleFilterChange(value: String?)

    abstract fun getDataFlow(filter: FILTER): Flow<PagingData<DOMAIN>>
}