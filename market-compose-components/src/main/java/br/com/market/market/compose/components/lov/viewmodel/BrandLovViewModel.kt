package br.com.market.market.compose.components.lov.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.domain.BrandDomain
import br.com.market.core.filter.BrandFilter
import br.com.market.market.common.repository.MarketRepository
import br.com.market.market.common.repository.lov.BrandLovRepository
import br.com.market.market.common.viewmodel.BaseSearchViewModel
import br.com.market.market.compose.components.lov.state.BrandLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrandLovViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val brandRepository: BrandLovRepository,
    private val marketRepository: MarketRepository
) : BaseSearchViewModel<BrandDomain, BrandFilter>() {

    private val _uiState: MutableStateFlow<BrandLovUIState> = MutableStateFlow(BrandLovUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val marketId = marketRepository.findFirst().first()?.id!!
            filter = BrandFilter(marketId = marketId)
        }.invokeOnCompletion {
            _uiState.update {
                it.copy(
                    brands = getDataFlow(filter)
                )
            }
        }
    }


    override fun onSimpleFilterChange(value: String?) {
        filter.simpleFilter = value

        _uiState.value = _uiState.value.copy(
            brands = getDataFlow(filter)
        )
    }

    override fun getDataFlow(filter: BrandFilter): Flow<PagingData<BrandDomain>> {
        return brandRepository.getConfiguredPager(context, filter).flow
    }
}