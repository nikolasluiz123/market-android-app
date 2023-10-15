package br.com.market.market.compose.components.lov.state

import androidx.paging.PagingData
import br.com.market.domain.BrandDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BrandLovUIState(
    val brands: Flow<PagingData<BrandDomain>> = emptyFlow()
)