package br.com.market.core.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.market.core.R
import br.com.market.core.ui.states.LoadImageLinkUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoadImageLinkViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoadImageLinkUIState> = MutableStateFlow(LoadImageLinkUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onLinkChange = { _uiState.value = _uiState.value.copy(link = it) },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.link.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            linkErrorMessage = context.getString(R.string.load_image_screen_link_required_validation_message)
                        )
                    }

                    isValid
                }
            )
        }
    }
}