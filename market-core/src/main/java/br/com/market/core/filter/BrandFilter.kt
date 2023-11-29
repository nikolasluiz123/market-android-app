package br.com.market.core.filter

import br.com.market.core.filter.base.BaseSearchFilter

class BrandFilter(
    marketId: Long,
    simpleFilter: String? = null,
    var categoryId: String? = null
): BaseSearchFilter(simpleFilter, marketId)