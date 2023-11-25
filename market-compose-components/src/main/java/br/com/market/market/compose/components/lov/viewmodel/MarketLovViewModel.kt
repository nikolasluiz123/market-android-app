package br.com.market.market.compose.components.lov.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.domain.MarketLovDomain
import br.com.market.market.common.repository.lov.MarketLovRepository
import br.com.market.market.common.viewmodel.ISearchViewModel
import br.com.market.market.compose.components.lov.state.MarketLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MarketLovViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MarketLovRepository
) : ViewModel(), ISearchViewModel<MarketLovDomain, BaseSearchFilter> {

    private val _uiState: MutableStateFlow<MarketLovUIState> = MutableStateFlow(MarketLovUIState())
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

    override fun getDataFlow(filter: BaseSearchFilter): Flow<PagingData<MarketLovDomain>> {
        return repository.getConfiguredPager(context, filter).flow
    }
}