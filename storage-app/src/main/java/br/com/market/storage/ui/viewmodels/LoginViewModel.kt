package br.com.market.storage.ui.viewmodels

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.R
import br.com.market.storage.business.repository.UserRepository
import br.com.market.storage.business.services.response.AuthenticationResponse
import br.com.market.storage.ui.domains.UserDomain
import br.com.market.storage.ui.states.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onEmailChange = { _uiState.value = _uiState.value.copy(email = it) },
                onPasswordChange = { _uiState.value = _uiState.value.copy(password = it) },
                onToggleErrorDialog = { errorMessage ->
                    _uiState.value = _uiState.value.copy(
                        showErrorDialog = !_uiState.value.showErrorDialog,
                        serverError = errorMessage
                    )
                },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.email.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            emailErrorMessage = context.getString(R.string.login_screen_email_required_validation_message)
                        )
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            emailErrorMessage = context.getString(R.string.login_screen_email_invalid_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            emailErrorMessage = ""
                        )
                    }

                    if (_uiState.value.password.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            passwordErrorMessage = context.getString(R.string.login_screen_password_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            passwordErrorMessage = ""
                        )
                    }

                    isValid
                }
            )
        }
    }

    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        return userRepository.authenticate(userDomain)
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

}