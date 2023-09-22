package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.models.Address
import br.com.market.models.Client
import br.com.market.models.User
import br.com.market.sdo.AddressSDO
import br.com.market.sdo.ClientSDO
import br.com.market.sdo.UserSDO
import br.com.market.servicedataaccess.responses.extensions.getPersistenceResponseBody
import br.com.market.servicedataaccess.responses.extensions.getSingleValueResponseBody
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import br.com.market.servicedataaccess.services.IClientService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClientWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IClientService
) : BaseWebClient(context) {

    suspend fun save(address: Address, user: User, client: Client): PersistenceResponse {
        return persistenceServiceErrorHandlingBlock(
            codeBlock = {
                val clientSDO = ClientSDO(
                    address = AddressSDO(
                        state = address.state,
                        city = address.city,
                        cep = address.cep,
                        publicPlace = address.publicPlace,
                        number = address.number,
                        complement = address.complement,
                        active = address.active,
                        localId = address.id
                    ),
                    user = UserSDO(
                        localId = user.id,
                        name = user.name!!,
                        email = user.email!!,
                        password = user.password!!,
                        token = user.token
                    ),
                    cpf = client.cpf!!,
                    localId = client.id,
                    active = client.active
                )

                service.save(clientSDO).getPersistenceResponseBody()
            }
        )
    }

    suspend fun isUniqueEmail(email: String): SingleValueResponse<Boolean> {
        return singleValueServiceErrorHandlingBlock(
            codeBlock = {
                service.isUniqueEmail(email).getSingleValueResponseBody()
            }
        )
    }

    suspend fun isUniqueCPF(cpf: String): SingleValueResponse<Boolean> {
        return singleValueServiceErrorHandlingBlock(
            codeBlock = {
                service.isUniqueCPF(cpf).getSingleValueResponseBody()
            }
        )
    }

}