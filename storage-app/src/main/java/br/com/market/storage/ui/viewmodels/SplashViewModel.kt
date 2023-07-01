package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.market.storage.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    suspend fun deviceRegistered(): Boolean {
        return deviceRepository.findFirst().first()?.let {
            it.synchronized && it.companyId != null
        } ?: false
    }

}