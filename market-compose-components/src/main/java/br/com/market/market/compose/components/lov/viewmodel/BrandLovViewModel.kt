package br.com.market.market.compose.components.lov.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.domain.BrandDomain
import br.com.market.market.common.repository.lov.BrandLovRepository
import br.com.market.market.common.viewmodel.ISearchViewModel
import br.com.market.market.compose.components.lov.state.BrandLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BrandLovViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val brandRepository: BrandLovRepository
) : ViewModel(), ISearchViewModel<BrandDomain, BaseSearchFilter> {

    private val _uiState: MutableStateFlow<BrandLovUIState> = MutableStateFlow(BrandLovUIState())
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

    override fun getDataFlow(filter: BaseSearchFilter): Flow<PagingData<BrandDomain>> {
        return brandRepository.getConfiguredPager(context, filter).flow
    }
}