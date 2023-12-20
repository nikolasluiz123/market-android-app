package br.com.market.servicedataaccess.services.params

import br.com.market.core.filter.MovementFilters
import br.com.market.servicedataaccess.services.params.interfaces.IServicePaginatedSearchParams

data class StorageOperationsHistoryServiceSearchParams(
    var filters: MovementFilters,
    override var limit: Int? = null,
    override var offset: Int? = null,
): IServicePaginatedSearchParams