package br.com.market.storage.repository

import br.com.market.localdataaccess.dao.DeviceDAO
import br.com.market.models.Device
import br.com.market.servicedataaccess.webclients.DeviceWebClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val deviceDAO: DeviceDAO,
    private val deviceWebClient: DeviceWebClient
) {

    fun findFirst(): Flow<Device?> {
        return deviceDAO.findFirst()
    }

    suspend fun sync(deviceId: String) {
        val response = deviceWebClient.findById(deviceId)

        if (response.success && response.values.isNotEmpty()) {
            deviceDAO.save(device = response.values.first())
        }
    }
}