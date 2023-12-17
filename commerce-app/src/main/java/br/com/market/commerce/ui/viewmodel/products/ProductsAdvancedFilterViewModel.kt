package br.com.market.commerce.ui.viewmodel.products

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.commerce.ui.navigation.products.argumentProductsAdvancedFilterJson
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.extensions.searchWordsInText
import br.com.market.core.gson.adapter.LocalDateTimeAdapter
import br.com.market.core.inputs.CommonAdvancedFilterItem
import br.com.market.core.ui.states.filter.AdvancedFilterUIState
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProductsAdvancedFilterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState: MutableStateFlow<AdvancedFilterUIState> = MutableStateFlow(AdvancedFilterUIState())

    val uiState get() = _uiState.asStateFlow()
    private val filterJson: String? = savedStateHandle[argumentProductsAdvancedFilterJson]
    private val filters = mutableListOf<CommonAdvancedFilterItem<Any?>>()

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        filterJson?.fromJsonNavParamToArgs(MovementSearchScreenFilters::class.java, gson)?.let { filter ->

            _uiState.update { currentState ->
                currentState.copy(filters = filters)
            }
        }
    }

    fun onSearch(filter: String?) {
        _uiState.update {
            it.copy(filters = getFilteredFilters(filter))
        }
    }

    private fun getFilteredFilters(filterText: String?): List<CommonAdvancedFilterItem<Any?>> {
        if (filterText.isNullOrEmpty()) {
            return filters
        }

        return filters.filter {
            context.resources.getString(it.labelResId).searchWordsInText(filterText)
        }
    }
}