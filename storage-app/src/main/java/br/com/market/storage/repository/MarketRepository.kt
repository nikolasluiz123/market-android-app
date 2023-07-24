package br.com.market.storage.repository

import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.models.Address
import br.com.market.models.Market
import br.com.market.servicedataaccess.webclients.MarketWebClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketRepository @Inject constructor(
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val marketWebClient: MarketWebClient
) {

    fun findFirst(): Flow<Market?> {
        return marketDAO.findFirst()
    }

    suspend fun findAddress(addressId: String): Address {
        return addressDAO.findById(addressId)
    }

    suspend fun sync(deviceId: String) {
        val response = marketWebClient.findByDeviceId(deviceId)

        if (response.success && response.values.isNotEmpty()) {
            with(response.values.first()) {
                val market = Market(
                    id = id,
                    name = name,
                    companyId = companyId,
                    addressId = address?.localId
                )

                val address = Address(
                    id = address?.localId!!,
                    state = address?.state,
                    city = address?.city,
                    publicPlace = address?.publicPlace,
                    number = address?.number,
                    complement = address?.complement,
                    cep = address?.cep,
                    synchronized = true,
                    active = address?.active!!
                )

                addressDAO.save(address)
                marketDAO.save(market)
            }
        }
    }
}