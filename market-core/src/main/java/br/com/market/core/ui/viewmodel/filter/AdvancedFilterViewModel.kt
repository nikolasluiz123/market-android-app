package br.com.market.core.ui.viewmodel.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.filter.AdvancedFiltersScreenArgs
import br.com.market.core.filter.formatter.IAdvancedFilterFormatter
import br.com.market.core.gson.GsonTypeAdapterAdapter
import br.com.market.core.ui.navigation.argumentFilters
import br.com.market.core.ui.states.filter.AdvancedFilterUIState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AdvancedFilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AdvancedFilterUIState> = MutableStateFlow(AdvancedFilterUIState())
    val uiState get() = _uiState.asStateFlow()

    private val filterArgs: String? = savedStateHandle[argumentFilters]

    init {
        this.fromJsonNavParamToAdvancedFiltersArgs()?.let { args ->
            _uiState.update { currentState ->
                currentState.copy(filters = args.filters)
            }
        }
    }

    private fun fromJsonNavParamToAdvancedFiltersArgs(): AdvancedFiltersScreenArgs? {
        val gson = Gson()
            .newBuilder()
            .registerTypeAdapter(IAdvancedFilterFormatter::class.java, GsonTypeAdapterAdapter())
            .create()

        return filterArgs?.fromJsonNavParamToArgs(AdvancedFiltersScreenArgs::class.java, gson)
    }

}
