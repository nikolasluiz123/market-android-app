package br.com.market.market.common.repository.lov

import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.UserDAO
import br.com.market.servicedataaccess.webclients.UserWebClient
import javax.inject.Inject

class UserLovRepository @Inject constructor(
    private val dao: UserDAO,
    private val marketDAO: MarketDAO,
    private val webClient: UserWebClient
) {

}