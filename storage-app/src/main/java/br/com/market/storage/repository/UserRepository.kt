package br.com.market.storage.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import br.com.market.core.pagination.PagingConfigUtils
import br.com.market.domain.UserDomain
import br.com.market.localdataaccess.dao.AddressDAO
import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.localdataaccess.dao.DeviceDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.UserDAO
import br.com.market.models.Address
import br.com.market.models.Company
import br.com.market.models.Device
import br.com.market.models.Market
import br.com.market.models.ThemeDefinitions
import br.com.market.models.User
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.webclients.UserWebClient
import br.com.market.storage.pagination.UserPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Classe Repository responsável por permitir manipular
 * o usuário, atualmente somente via WebClient.
 *
 * @property context Context do App para uso geral.
 * @property webClient WebClient para acesso aos end points do usuário no serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
class UserRepository @Inject constructor(
    private val userDAO: UserDAO,
    private val companyDAO: CompanyDAO,
    private val deviceDAO: DeviceDAO,
    private val marketDAO: MarketDAO,
    private val addressDAO: AddressDAO,
    private val webClient: UserWebClient
) {

    /**
     * Função responsável por autenticar um usuário, iniciando sua seção.
     *
     * @param userDomain Objeto com os dados do usuário recuperados da tela.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun authenticate(userDomain: UserDomain): AuthenticationResponse {
        val response = webClient.authenticate(userDomain = userDomain)

        if (response.success) {
            with(response.result!!) {
                val themeDefinitions = ThemeDefinitions(
                    id = company.themeDefinitions.id,
                    colorPrimary = company.themeDefinitions.colorPrimary,
                    colorSecondary = company.themeDefinitions.colorSecondary,
                    colorTertiary = company.themeDefinitions.colorTertiary,
                    imageLogo = company.themeDefinitions.imageLogo
                )

                val company = Company(
                    id = company.id,
                    name = company.name,
                    themeDefinitionsId = themeDefinitions.id
                )

                val address = Address(
                    id = market.address?.localId!!,
                    state = market.address?.state,
                    city = market.address?.city,
                    publicPlace = market.address?.publicPlace,
                    number = market.address?.number,
                    complement = market.address?.complement,
                    cep = market.address?.cep
                )

                val market = Market(
                    id = market.id,
                    companyId = company.id,
                    addressId = address.id,
                    name = market.name
                )

                val device = Device(
                    id = device.id!!,
                    marketId = market.id,
                    name = device.name
                )

                val user = User(
                    id = user.localId,
                    name = user.name,
                    email = user.email,
                    password = user.password,
                    token = user.token,
                    marketId = market.id
                )

                companyDAO.saveTheme(themeDefinitions)
                companyDAO.saveCompany(company)
                addressDAO.save(address)
                marketDAO.save(market)
                deviceDAO.save(device)
                userDAO.saveUsers(user)
            }
        }

        return response
    }

    suspend fun sync(): MarketServiceResponse {
        return updateUsersOfLocalDB()
    }

    private suspend fun updateUsersOfLocalDB(): MarketServiceResponse {
        val responseFindAllProducts = webClient.findAllUsers(marketDAO.findFirst().first()?.id!!)

        if (responseFindAllProducts.success) {
            userDAO.saveUsers(responseFindAllProducts.values)
        }

        return responseFindAllProducts.toBaseResponse()
    }

    fun findUsers(name: String? = null): Flow<PagingData<UserDomain>> {
        return Pager(
            config = PagingConfigUtils.defaultPagingConfig(),
            pagingSourceFactory = { UserPagingSource(dao = userDAO, name = name) }
        ).flow
    }
}