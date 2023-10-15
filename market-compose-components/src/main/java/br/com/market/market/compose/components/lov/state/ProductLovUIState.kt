package br.com.market.market.compose.components.lov.state

import androidx.paging.PagingData
import br.com.market.domain.ProductImageReadDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProductLovUIState(
    val products: Flow<PagingData<ProductImageReadDomain>> = emptyFlow()
)