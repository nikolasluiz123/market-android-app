package br.com.market.storage.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.R
import br.com.market.storage.business.repository.UserRepository
import br.com.market.storage.business.services.response.AuthenticationResponse
import br.com.market.storage.ui.domains.UserDomain
import br.com.market.storage.ui.states.RegisterUserUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegisterUserUIState> = MutableStateFlow(RegisterUserUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->

            currentState.copy(
                onNameChange = { _uiState.value = _uiState.value.copy(name = it) },
                onEmailChange = { _uiState.value = _uiState.value.copy(email = it) },
                onPasswordChange = { _uiState.value = _uiState.value.copy(password = it) },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.name.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            nameErrorMessage = context.getString(R.string.register_user_screen_name_required_validation_message)
                        )
                    }

                    if (_uiState.value.email.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            emailErrorMessage = context.getString(R.string.register_user_screen_email_required_validation_message)
                        )
                    }

                    if (_uiState.value.password.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            passwordErrorMessage = context.getString(R.string.register_user_screen_password_required_validation_message)
                        )
                    }

                    if (isValid) {
                        _uiState.value = _uiState.value.copy(
                            nameErrorMessage = "",
                            emailErrorMessage = "",
                            passwordErrorMessage = ""
                        )
                    }

                    isValid
                })
        }
    }

    suspend fun registerUser(userDomain: UserDomain): AuthenticationResponse {
        return userRepository.registerUser(userDomain)
    }

}