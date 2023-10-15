package br.com.market.market.common.repository

import androidx.paging.Pager
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.domain.base.BaseDomain

interface IPagedRemoteSearchRepository<FILTER: BaseSearchFilter, DOMAIN: BaseDomain> {

    fun getConfiguredPager(filters: FILTER): Pager<Int, DOMAIN>
}