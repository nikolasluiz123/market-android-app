package br.com.market.core.filter

import br.com.market.core.filter.base.BaseSearchFilter

class ProductFilter(
    marketId: Long,
    simpleFilter: String? = null,
    var categoryId: String,
    var brandId: String
): BaseSearchFilter(simpleFilter, marketId)