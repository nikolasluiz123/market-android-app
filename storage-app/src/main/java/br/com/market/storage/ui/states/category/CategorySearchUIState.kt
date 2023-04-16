package br.com.market.storage.ui.states.category

import androidx.paging.PagingData
import br.com.market.domain.CategoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CategorySearchUIState(
    val categories: Flow<PagingData<CategoryDomain>> = emptyFlow()
)