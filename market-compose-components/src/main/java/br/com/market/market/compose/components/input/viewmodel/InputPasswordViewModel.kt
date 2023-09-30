package br.com.market.market.compose.components.input.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.inputs.arguments.InputPasswordArgs
import br.com.market.core.ui.states.input.InputPasswordUIState
import br.com.market.market.compose.components.navigation.inputPasswordArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InputPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<InputPasswordUIState> = MutableStateFlow(InputPasswordUIState())
    val uiState get() = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[inputPasswordArguments]

    init {
        jsonArgs?.fromJsonNavParamToArgs(InputPasswordArgs::class.java)?.let { args ->
            _uiState.update { currentState ->
                currentState.copy(
                    titleResId = args.titleResId,
                    value = args.value as String?,
                    maxLength = args.maxLength,
                    keyboardOptions = args.keyboardOptions,
                    onValueChange = {
                        _uiState.value = _uiState.value.copy(value = it)
                    }
                )
            }
        }
    }
}
