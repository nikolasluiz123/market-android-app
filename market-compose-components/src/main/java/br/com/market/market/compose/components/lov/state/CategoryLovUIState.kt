package br.com.market.market.compose.components.lov.state

import androidx.paging.PagingData
import br.com.market.domain.CategoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CategoryLovUIState(
    val brands: Flow<PagingData<CategoryDomain>> = emptyFlow()
)