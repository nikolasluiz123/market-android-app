package br.com.market.storage.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.domain.UserDomain
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.storage.R
import br.com.market.storage.repository.UserRepository
import br.com.market.storage.ui.states.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel da tela de login
 *
 * @property userRepository Classe responsável por realizar operações referentes ao Usuário.
 *
 * @param context Contexto do APP
 *
 * @author Nikolas Luiz Schmitt
 */
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
                onEmailChange = { _uiState.value = _uiState.value.copy(email = it) },
                onPasswordChange = { _uiState.value = _uiState.value.copy(password = it) },
                onToggleMessageDialog = { errorMessage ->
                    _uiState.value = _uiState.value.copy(
                        showDialogMessage = !_uiState.value.showDialogMessage,
                        serverMessage = errorMessage
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
        val response = userRepository.authenticate(userDomain)

        if (response.success) {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKey.TOKEN] = response.token!!
                preferences[PreferencesKey.USER] = response.userLocalId!!
            }
        }

        return response
    }
}