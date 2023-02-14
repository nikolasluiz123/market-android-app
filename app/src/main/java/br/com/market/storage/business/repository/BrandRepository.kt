package br.com.market.storage.business.repository

import br.com.market.storage.business.dao.BrandDAO
import br.com.market.storage.business.webclient.BrandWebClient
import javax.inject.Inject

class BrandRepository @Inject constructor(
    private val brandDAO: BrandDAO,
    private val brandWebClient: BrandWebClient
) {

}