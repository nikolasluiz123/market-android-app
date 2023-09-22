package br.com.market.core.ui.viewmodel.input

import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.fromJsonNavParamToArgs
import br.com.market.core.filter.arguments.InputArgs
import br.com.market.core.gson.InterfaceAdapter
import br.com.market.core.ui.navigation.inputTextArguments
import br.com.market.core.ui.states.input.InputTextUIState
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InputTextViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<InputTextUIState> = MutableStateFlow(InputTextUIState())
    val uiState get() = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[inputTextArguments]

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(VisualTransformation::class.java, InterfaceAdapter<VisualTransformation>())
            .create()

        jsonArgs?.fromJsonNavParamToArgs(InputArgs::class.java, gson)?.let { args ->
            _uiState.update { currentState ->
                currentState.copy(
                    titleResId = args.titleResId,
                    value = args.value as String?,
                    maxLength = args.maxLength,
                    keyboardOptions = args.keyboardOptions,
                    visualTransformation = args.visualTransformation,
                    onValueChange = {
                        _uiState.value = _uiState.value.copy(value = it)
                    }
                )
            }
        }
    }
}
