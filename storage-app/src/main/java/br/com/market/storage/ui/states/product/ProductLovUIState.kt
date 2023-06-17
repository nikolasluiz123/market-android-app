package br.com.market.storage.ui.states.product

import androidx.paging.PagingData
import br.com.market.localdataaccess.tuples.ProductImageTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProductLovUIState(
    val products: Flow<PagingData<ProductImageTuple>> = emptyFlow()
)