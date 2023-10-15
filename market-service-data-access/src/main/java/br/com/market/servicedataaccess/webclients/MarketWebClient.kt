package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.sdo.MarketReadSDO
import br.com.market.sdo.MarketSDO
import br.com.market.servicedataaccess.responses.extensions.getReadResponseBody
import br.com.market.servicedataaccess.responses.types.ReadResponse
import br.com.market.servicedataaccess.services.IMarketService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MarketWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IMarketService
) : BaseWebClient(context) {

    suspend fun findByDeviceId(deviceId: String): ReadResponse<MarketSDO> {
        return readServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.findByDeviceId(deviceId).getReadResponseBody()

                ReadResponse(
                    values = response.values,
                    code = response.code,
                    success = response.success,
                    error = response.error
                )
            }
        )
    }

    suspend fun getListLovMarketReadSDO(simpleFilter: String?, limit: Int, offset: Int): ReadResponse<MarketReadSDO> {
        return service.getListLovMarketReadDTO(getToken(), simpleFilter, limit, offset).getReadResponseBody()
    }
}