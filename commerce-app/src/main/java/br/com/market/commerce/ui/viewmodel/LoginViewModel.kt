package br.com.market.commerce.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.commerce.repository.UserRepository
import br.com.market.commerce.ui.state.LoginUiState
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.core.ui.states.Field
import br.com.market.domain.UserDomain
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                email = Field(onChange = { _uiState.value = _uiState.value.copy(email = _uiState.value.email.copy(value = it)) }),
                password = Field(onChange = { _uiState.value = _uiState.value.copy(password = _uiState.value.password.copy(value = it)) }),
                onShowDialog = { type, message, onConfirm, onCancel ->
                    _uiState.value = _uiState.value.copy(
                        dialogType = type,
                        showDialog = true,
                        dialogMessage = message,
                        onConfirm = onConfirm,
                        onCancel = onCancel
                    )
                },
                onHideDialog = { _uiState.value = _uiState.value.copy(showDialog = false) },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onValidate = {
                    var isValid = true

                    validateEmail { isValid = it }
                    validatePassword { isValid = it }

                    isValid
                }
            )
        }
    }

    private fun validateEmail(onValidChange: (Boolean) -> Unit) {
        viewModelScope.launch {
            when {
                _uiState.value.email.valueIsEmpty() -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(errorMessage = context.getString(br.com.market.core.R.string.login_screen_email_required_validation_message))
                    )
                }

                !Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email.value).matches() -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(errorMessage = context.getString(br.com.market.core.R.string.login_screen_email_invalid_validation_message))
                    )
                }

                else -> {
                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(errorMessage = "")
                    )
                }
            }
        }
    }

    private fun validatePassword(onValidChange: (Boolean) -> Unit) {
        when {
            _uiState.value.password.valueIsEmpty() -> {
                onValidChange(false)

                _uiState.value = _uiState.value.copy(
                    password = _uiState.value.password.copy(errorMessage = context.getString(br.com.market.core.R.string.login_screen_password_required_validation_message))
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    password = _uiState.value.password.copy(errorMessage = "")
                )
            }
        }
    }

    fun authenticate(userDomain: UserDomain, callback: (AuthenticationResponse) -> Unit) {
        viewModelScope.launch {
            val response = userRepository.authenticate(userDomain)

            if (response.success) {
                context.dataStore.edit { preferences ->
                    preferences[PreferencesKey.TOKEN] = response.result?.token!!
                    preferences[PreferencesKey.USER] = response.result?.userLocalId!!
                }
            }

            callback(response)
        }
    }
}