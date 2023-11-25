package br.com.market.storage.ui.viewmodels.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.core.filter.BaseSearchFilter
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.domain.CategoryDomain
import br.com.market.market.common.viewmodel.ISearchViewModel
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.MarketRepository
import br.com.market.storage.ui.states.category.CategorySearchUIState
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
@SuppressLint("StaticFieldLeak")
class CategorySearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryRepository: CategoryRepository,
    private val marketRepository: MarketRepository
) : ViewModel(), ISearchViewModel<CategoryDomain, BaseSearchFilter> {

    private val _uiState: MutableStateFlow<CategorySearchUIState> = MutableStateFlow(CategorySearchUIState())
    val uiState get() = _uiState.asStateFlow()

    private lateinit var filter: BaseSearchFilter

    init {
        viewModelScope.launch {
            val marketId = marketRepository.findFirst().first()?.id!!
            filter = BaseSearchFilter(marketId = marketId)
        }.invokeOnCompletion {
            _uiState.update {
                it.copy(
                    categories = getDataFlow(filter)
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKey.TOKEN] = ""
                preferences[PreferencesKey.USER] = ""
            }
        }
    }

    override fun onSimpleFilterChange(value: String?) {
        filter.simpleFilter = value

        _uiState.value = _uiState.value.copy(
            categories = getDataFlow(filter)
        )
    }

    override fun getDataFlow(filter: BaseSearchFilter): Flow<PagingData<CategoryDomain>> {
        return categoryRepository.getConfiguredPager(context, filter).flow
    }
}