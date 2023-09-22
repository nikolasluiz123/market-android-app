package br.com.market.commerce.repository

import br.com.market.domain.AddressDomain
import br.com.market.domain.ClientDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.ClientDAO
import br.com.market.localdataaccess.dao.UserDAO
import br.com.market.models.Address
import br.com.market.models.Client
import br.com.market.models.User
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import br.com.market.servicedataaccess.webclients.ClientWebClient
import br.com.market.servicedataaccess.webclients.ViaCepWebClient
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val clientDAO: ClientDAO,
    private val addressDAO: AddressDAO,
    private val userDAO: UserDAO,
    private val clientWebClient: ClientWebClient,
    private val viaCepWebClient: ViaCepWebClient
) {

    suspend fun save(clientDomain: ClientDomain): PersistenceResponse {
        val user = if(clientDomain.userDomain.id != null) {
            userDAO.findUserByEmail(clientDomain.userDomain.email)!!
        } else {
            User(
                name = clientDomain.userDomain.name,
                email = clientDomain.userDomain.email,
                password = clientDomain.userDomain.password,
            )
        }

        val address = if(clientDomain.addressDomain.id != null) {
            addressDAO.findById(clientDomain.addressDomain.id!!)
        } else {
            Address(
                cep = clientDomain.addressDomain.cep,
                state = clientDomain.addressDomain.state,
                city = clientDomain.addressDomain.city,
                publicPlace = clientDomain.addressDomain.publicPlace,
                complement = clientDomain.addressDomain.complement,
                number = clientDomain.addressDomain.number
            )
        }

        val client = if(clientDomain.id != null) {
            clientDAO.findById(clientDomain.id!!)
        } else {
            Client(
                userId = user.id,
                addressId = address.id,
                cpf = clientDomain.cpf
            )
        }

        val response = clientWebClient.save(address, user, client)

        if (response.success) {
            val objectSynchronized = response.getObjectSynchronized()

            address.synchronized = objectSynchronized
            client.synchronized = objectSynchronized

            userDAO.save(user)
            addressDAO.save(address)
            clientDAO.save(client)
        }

        return response
    }

    suspend fun isUniqueEmail(email: String): Boolean {
        val response = clientWebClient.isUniqueEmail(email)
        return response.value ?: false
    }

    suspend fun isUniqueCPF(cpf: String): Boolean {
        val response = clientWebClient.isUniqueCPF(cpf)
        return response.value ?: false
    }

    suspend fun getAddressByCep(cep: String): SingleValueResponse<AddressDomain> {
        return viaCepWebClient.getAddressByCep(cep)
    }

}