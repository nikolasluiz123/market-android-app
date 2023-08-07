package br.com.market.core.ui.viewmodel.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.filter.AdvancedFilterArgs
import br.com.market.core.ui.navigation.dateRangeAdvancedFilterArguments
import br.com.market.core.ui.states.filter.DateRangeAdvancedFilterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DateRangeAdvancedFilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<DateRangeAdvancedFilterUIState> = MutableStateFlow(DateRangeAdvancedFilterUIState())
    val uiState get() = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[dateRangeAdvancedFilterArguments]

    init {
        jsonArgs?.fromJsonNavParamToArgs(AdvancedFilterArgs::class.java)?.let { args ->
            _uiState.update { currentState ->
                currentState.copy(
                    titleResId = args.titleResId,
                )
            }
        }
    }
}
