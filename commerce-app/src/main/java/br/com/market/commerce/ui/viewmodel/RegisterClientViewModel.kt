package br.com.market.commerce.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.commerce.R
import br.com.market.commerce.repository.ClientRepository
import br.com.market.commerce.ui.state.RegisterClientUiState
import br.com.market.commerce.util.CEPUtil
import br.com.market.commerce.util.CPFUtil
import br.com.market.core.ui.states.Field
import br.com.market.domain.AddressDomain
import br.com.market.domain.ClientDomain
import br.com.market.domain.UserDomain
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class RegisterClientViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegisterClientUiState> = MutableStateFlow(RegisterClientUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                name = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(name = _uiState.value.name.copy(value = it, errorMessage = ""))
                    }
                ),
                cpf = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(cpf = _uiState.value.cpf.copy(value = it, errorMessage = ""))
                    }
                ),
                email = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(email = _uiState.value.email.copy(value = it, errorMessage = ""))
                    },
                ),
                password = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(password = _uiState.value.password.copy(value = it, errorMessage = ""))
                    },
                ),
                cep = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(cep = _uiState.value.cep.copy(value = it, errorMessage = ""))
                    }
                ),
                state = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(state = _uiState.value.state.copy(value = it, errorMessage = ""))
                    }
                ),
                city = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(city = _uiState.value.city.copy(value = it, errorMessage = ""))
                    }
                ),
                publicPlace = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(publicPlace = _uiState.value.publicPlace.copy(value = it, errorMessage = ""))
                    }
                ),
                number = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(number = _uiState.value.number.copy(value = it, errorMessage = ""))
                    }
                ),
                complement = Field(
                    onChange = {
                        _uiState.value = _uiState.value.copy(complement = _uiState.value.complement.copy(value = it, errorMessage = ""))
                    }
                ),
                onValidate = {
                    var isValid = true

                    validateName { isValid = it }
                    validateCpf { isValid = it }
                    validateEmail { isValid = it }
                    validatePassword { isValid = it }
                    validateCep { isValid = it }
                    validateState { isValid = it }
                    validateCity { isValid = it }
                    validatePublicPlace { isValid = it }
                    validateNumber { isValid = it }

                    isValid
                },
                onToggleMessageDialog = { errorMessage ->
                    _uiState.value = _uiState.value.copy(
                        showDialogMessage = !_uiState.value.showDialogMessage,
                        serverMessage = errorMessage
                    )
                },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) }
            )
        }
    }

    private fun validateName(onValidChange: (Boolean) -> Unit) {
        when {
            _uiState.value.name.valueIsEmpty() -> {
                onValidChange(false)

                _uiState.value = _uiState.value.copy(
                    name = _uiState.value.name.copy(errorMessage = context.getString(R.string.register_client_name_required_validation_message))
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    name = _uiState.value.name.copy(errorMessage = "")
                )
            }
        }
    }

    private fun validateCpf(onValidChange: (Boolean) -> Unit) {
        viewModelScope.launch {
            when {
                _uiState.value.cpf.valueIsEmpty() -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        cpf = _uiState.value.cpf.copy(errorMessage = context.getString(R.string.register_client_cpf_required_validation_message))
                    )
                }

                !CPFUtil.isValid(_uiState.value.cpf.value) -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        cpf = _uiState.value.cpf.copy(errorMessage = context.getString(R.string.register_client_cpf_invalid_validation_message))
                    )
                }

                !clientRepository.isUniqueCPF(CPFUtil.unFormat(_uiState.value.cpf.value)) -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        cpf = _uiState.value.cpf.copy(errorMessage = context.getString(R.string.register_client_cpf_registred_validation_message))
                    )
                }

                else -> {
                    _uiState.value = _uiState.value.copy(
                        cpf = _uiState.value.cpf.copy(errorMessage = "")
                    )
                }
            }
        }
    }

    private fun validateEmail(onValidChange: (Boolean) -> Unit) {
        viewModelScope.launch {
            when {
                _uiState.value.email.valueIsEmpty() -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(errorMessage = context.getString(R.string.register_client_email_required_validation_message))
                    )
                }

                !Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email.value).matches() -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(errorMessage = context.getString(R.string.register_client_email_invalid_validation_message))
                    )
                }

                !clientRepository.isUniqueEmail(_uiState.value.email.value) -> {
                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(errorMessage = context.getString(R.string.register_client_email_registred_validation_message))
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
                    password = _uiState.value.password.copy(errorMessage = context.getString(R.string.register_client_password_required_validation_message))
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    password = _uiState.value.password.copy(errorMessage = "")
                )
            }
        }
    }

    private fun validateCep(onValidChange: (Boolean) -> Unit) {
        when {
            _uiState.value.cep.valueIsEmpty() -> {
                onValidChange(false)

                _uiState.value = _uiState.value.copy(
                    cep = _uiState.value.cep.copy(errorMessage = context.getString(R.string.register_client_cep_required_validation_message))
                )
            }

            !CEPUtil.isValid(_uiState.value.cep.value) -> {
                onValidChange(false)

                _uiState.value = _uiState.value.copy(
                    cep = _uiState.value.cep.copy(errorMessage = context.getString(R.string.register_client_cep_invalid_validation_message))
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    cep = _uiState.value.cep.copy(errorMessage = "")
                )
            }
        }
    }

    private fun validateState(onValidChange: (Boolean) -> Unit) {
        if (_uiState.value.state.valueIsEmpty()) {
            onValidChange(false)

            _uiState.value = _uiState.value.copy(
                state = _uiState.value.state.copy(errorMessage = context.getString(R.string.register_client_state_required_validation_message))
            )
        } else {
            _uiState.value = _uiState.value.copy(
                state = _uiState.value.state.copy(errorMessage = "")
            )
        }
    }

    private fun validateCity(onValidChange: (Boolean) -> Unit) {
        if (_uiState.value.city.valueIsEmpty()) {
            onValidChange(false)

            _uiState.value = _uiState.value.copy(
                city = _uiState.value.city.copy(errorMessage = context.getString(R.string.register_client_city_required_validation_message))
            )
        } else {
            _uiState.value = _uiState.value.copy(
                city = _uiState.value.city.copy(errorMessage = "")
            )
        }
    }

    private fun validatePublicPlace(onValidChange: (Boolean) -> Unit) {
        if (_uiState.value.publicPlace.valueIsEmpty()) {
            onValidChange(false)

            _uiState.value = _uiState.value.copy(
                publicPlace = _uiState.value.publicPlace.copy(errorMessage = context.getString(R.string.register_client_public_place_required_validation_message))
            )
        } else {
            _uiState.value = _uiState.value.copy(
                publicPlace = _uiState.value.publicPlace.copy(errorMessage = "")
            )
        }
    }

    private fun validateNumber(onValidChange: (Boolean) -> Unit) {
        if (_uiState.value.number.valueIsEmpty()) {
            onValidChange(false)

            _uiState.value = _uiState.value.copy(
                number = _uiState.value.number.copy(errorMessage = context.getString(R.string.register_client_number_required_validation_message))
            )
        } else {
            _uiState.value = _uiState.value.copy(
                number = _uiState.value.number.copy(errorMessage = "")
            )
        }
    }

    fun save(callback: (PersistenceResponse) -> Unit) {
        val clientDomain = ClientDomain(
            userDomain = UserDomain(
                name = _uiState.value.name.value,
                email = _uiState.value.email.value,
                password = _uiState.value.password.value
            ),
            addressDomain = AddressDomain(
                cep = _uiState.value.cep.value,
                state = _uiState.value.state.value,
                city = _uiState.value.city.value,
                publicPlace = _uiState.value.publicPlace.value,
                number = _uiState.value.number.value,
                complement = _uiState.value.complement.value
            ),
            cpf = _uiState.value.cpf.value
        )

        viewModelScope.launch {
            val response = clientRepository.save(clientDomain)

            withContext(Main) {
                callback(response)
            }
        }
    }

    fun onSearchAddressInformationByCep(callback: (MarketServiceResponse) -> Unit) {
        var isValid = true
        validateCep { isValid = it }

        if (isValid) {
            viewModelScope.launch {
                val cep = CEPUtil.unFormat(_uiState.value.cep.value)

                val response = clientRepository.getAddressByCep(cep)

                if (response.success) {
                    response.value?.let { domain ->
                        _uiState.value.state.onChange(domain.state)
                        _uiState.value.city.onChange(domain.city)
                        _uiState.value.publicPlace.onChange(domain.publicPlace)
                        _uiState.value.complement.onChange(domain.complement ?: "")
                    }
                }

                withContext(Main) {
                    callback(response.toBaseResponse())
                }
            }
        }
    }
}