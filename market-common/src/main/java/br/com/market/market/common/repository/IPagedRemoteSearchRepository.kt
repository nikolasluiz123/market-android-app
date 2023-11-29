package br.com.market.market.common.repository

import android.content.Context
import androidx.paging.Pager
import br.com.market.core.filter.base.BaseSearchFilter
import br.com.market.domain.base.BaseDomain

interface IPagedRemoteSearchRepository<FILTER: BaseSearchFilter, DOMAIN: BaseDomain> {

    fun getConfiguredPager(context: Context, filters: FILTER): Pager<Int, DOMAIN>
}