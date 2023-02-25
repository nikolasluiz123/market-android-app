package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.market.storage.ui.states.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onEmailChange = { _uiState.value = _uiState.value.copy(email = it) },
                onPasswordChange = { _uiState.value = _uiState.value.copy(password = it) },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.email.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            emailErrorMessage = "O e-mail é obrigatório."
                        )
                    }

                    if (_uiState.value.password.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            passwordErrorMessage = "A senha é obrigatória."
                        )
                    }

                    isValid
                }
            )
        }
    }

}