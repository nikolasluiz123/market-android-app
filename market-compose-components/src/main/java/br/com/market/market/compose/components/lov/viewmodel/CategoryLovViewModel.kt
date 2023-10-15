package br.com.market.market.compose.components.lov.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.domain.CategoryDomain
import br.com.market.market.common.repository.lov.CategoryLovRepository
import br.com.market.market.common.viewmodel.ISearchViewModel
import br.com.market.market.compose.components.lov.state.CategoryLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CategoryLovViewModel @Inject constructor(
    private val repository: CategoryLovRepository
) : ViewModel(), ISearchViewModel<CategoryDomain, BaseSearchFilter> {

    private val _uiState: MutableStateFlow<CategoryLovUIState> = MutableStateFlow(CategoryLovUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                brands = getDataFlow(BaseSearchFilter())
            )
        }
    }


    override fun onSimpleFilterChange(value: String?) {
        _uiState.value = _uiState.value.copy(
            brands = getDataFlow(BaseSearchFilter(simpleFilter = value))
        )
    }

    override fun getDataFlow(filter: BaseSearchFilter): Flow<PagingData<CategoryDomain>> {
        return repository.getConfiguredPager(filter).flow
    }
}