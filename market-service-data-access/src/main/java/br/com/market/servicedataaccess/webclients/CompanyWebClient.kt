package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.sdo.CompanySDO
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.ICompanyService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CompanyWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: ICompanyService
) : BaseWebClient(context) {

    suspend fun findByDeviceId(deviceId: String): ReadResponse<CompanySDO> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findByDeviceId(getToken(), deviceId).getReadResponseBody()

                ReadResponse(
                    values = response.values,
                    code = response.code,
                    success = response.success,
                    error = response.error
                )
            }
        )
    }
}