package br.com.market.market.compose.components.lov.state

import androidx.paging.PagingData
import br.com.market.domain.MarketLovDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MarketLovUIState(
    val brands: Flow<PagingData<MarketLovDomain>> = emptyFlow()
)