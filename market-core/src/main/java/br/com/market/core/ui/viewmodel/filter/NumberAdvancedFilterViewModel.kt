package br.com.market.core.ui.viewmodel.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.filter.NumberAdvancedFilterArgs
import br.com.market.core.ui.navigation.numberAdvancedFilterArguments
import br.com.market.core.ui.states.filter.NumberAdvancedFilterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NumberAdvancedFilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<NumberAdvancedFilterUIState> = MutableStateFlow(NumberAdvancedFilterUIState())
    val uiState get() = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[numberAdvancedFilterArguments]

    init {
        jsonArgs?.fromJsonNavParamToArgs(NumberAdvancedFilterArgs::class.java)?.let { args ->
            _uiState.update { currentState ->
                currentState.copy(
                    titleResId = args.titleResId,
                    value = args.value as String? ?: "",
                    integer = args.integer,
                    onValueChange = {
                        _uiState.value = _uiState.value.copy(value = it)
                    }
                )
            }
        }
    }
}
