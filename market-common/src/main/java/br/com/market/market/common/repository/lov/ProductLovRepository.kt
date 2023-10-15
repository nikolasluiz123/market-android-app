package br.com.market.market.common.repository.lov

import br.com.market.localdataaccess.dao.BrandDAO
import br.com.market.localdataaccess.dao.MarketDAO
import br.com.market.localdataaccess.dao.ProductDAO
import br.com.market.localdataaccess.dao.ProductImageDAO
import br.com.market.market.common.repository.BaseRepository
import br.com.market.servicedataaccess.webclients.ProductWebClient
import javax.inject.Inject

class ProductLovRepository @Inject constructor(
    private val productDAO: ProductDAO,
    private val marketDAO: MarketDAO,
    private val productImageDAO: ProductImageDAO,
    private val brandDAO: BrandDAO,
    private val webClient: ProductWebClient
): BaseRepository() {


}