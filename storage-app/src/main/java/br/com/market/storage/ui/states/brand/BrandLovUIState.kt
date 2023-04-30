package br.com.market.storage.ui.states.brand

import androidx.paging.PagingData
import br.com.market.domain.BrandDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BrandLovUIState(
    val brands: Flow<PagingData<BrandDomain>> = emptyFlow()
)