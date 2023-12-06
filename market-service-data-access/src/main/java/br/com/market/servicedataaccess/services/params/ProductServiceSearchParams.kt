package br.com.market.servicedataaccess.services.params

import br.com.market.servicedataaccess.services.params.interfaces.IServicePaginatedSearchParams
import br.com.market.servicedataaccess.services.params.interfaces.IServiceQuickFilterParams

data class ProductServiceSearchParams(
    val categoryId: String? = null,
    val brandId: String? = null,
    override val quickFilter: String?,
    override var limit: Int? = null,
    override var offset: Int? = null,
): IServicePaginatedSearchParams, IServiceQuickFilterParams