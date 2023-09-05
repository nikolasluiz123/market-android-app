package br.com.market.storage.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.storage.repository.CompanyRepository
import br.com.market.storage.repository.DeviceRepository
import br.com.market.storage.repository.MarketRepository
import br.com.market.storage.repository.UserRepository
import br.com.market.storage.ui.navigation.argumentShowBack
import br.com.market.storage.ui.states.AboutUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class AboutViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository,
    private val companyRepository: CompanyRepository,
    private val marketRepository: MarketRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AboutUIState> = MutableStateFlow(AboutUIState())
    val uiState get() = _uiState.asStateFlow()

    private val showBack: String? = savedStateHandle[argumentShowBack]

    init {
        _uiState.update { currentState ->
            currentState.copy(showBack = showBack.toBoolean())
        }

        viewModelScope.launch {
            deviceRepository.findFirst().collect {
                if (it != null) {
                    _uiState.value = _uiState.value.copy(
                        deviceId = it.id,
                        deviceName = it.name!!
                    )
                } else {
                    val deviceId = context.dataStore.data.first()[PreferencesKey.TEMP_DEVICE_ID] ?: ""
                    _uiState.value = _uiState.value.copy(deviceId = deviceId)
                }
            }
        }

        viewModelScope.launch {
            companyRepository.findFirstCompany().collect {
                if (it != null) {
                    _uiState.value = _uiState.value.copy(
                        companyName = it.name
                    )
                }
            }
        }

        viewModelScope.launch {
            marketRepository.findFirst().collect {
                if (it != null) {
                    val address = marketRepository.findAddress(it.addressId!!)

                    _uiState.value = _uiState.value.copy(
                        marketName = it.name ?: "",
                        marketAddress = "${address.publicPlace}, ${address.number}"
                    )
                }
            }
        }

        viewModelScope.launch {
            companyRepository.findFirstThemeDefinition().collect {
                if (it != null) {
                    _uiState.value = _uiState.value.copy(
                        imageLogo = it.imageLogo
                    )
                }
            }
        }
    }

    fun sync(onFinishSync: () -> Unit) {
        viewModelScope.launch {
            val deviceId = context.dataStore.data.first()[PreferencesKey.TEMP_DEVICE_ID] ?: deviceRepository.findFirst().first()?.id!!

            companyRepository.sync(deviceId)
            marketRepository.sync(deviceId)
            deviceRepository.sync(deviceId)
            userRepository.sync()
        }.invokeOnCompletion { onFinishSync() }
    }
}
