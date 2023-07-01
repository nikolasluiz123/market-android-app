package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Device
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.IDeviceService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IDeviceService
) : BaseWebClient(context) {

    suspend fun findById(id: String): ReadResponse<Device> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findById(getToken(), id).getReadResponseBody()

                val device = response.values.firstOrNull()?.run {
                    Device(
                        id = id,
                        synchronized = true,
                        active = true,
                        companyId = companyId,
                        name = name
                    )
                }

                ReadResponse(
                    values = device?.let(::listOf) ?: emptyList(),
                    code = response.code,
                    success = response.success,
                    error = response.error
                )
            }
        )
    }
}