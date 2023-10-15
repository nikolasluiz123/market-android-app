package br.com.market.market.compose.components.input.viewmodel

import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.inputs.arguments.InputNumberArgs
import br.com.market.core.gson.adapter.InterfaceAdapter
import br.com.market.core.ui.states.input.InputNumberUIState
import br.com.market.market.compose.components.input.navigation.numberAdvancedFilterArguments
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InputNumberViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<InputNumberUIState> = MutableStateFlow(InputNumberUIState())
    val uiState get() = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[numberAdvancedFilterArguments]

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(VisualTransformation::class.java, InterfaceAdapter<VisualTransformation>())
            .create()

        jsonArgs?.fromJsonNavParamToArgs(InputNumberArgs::class.java, gson)?.let { args ->
            _uiState.update { currentState ->
                currentState.copy(
                    titleResId = args.titleResId,
                    value = args.value as String? ?: "",
                    integer = args.integer,
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
