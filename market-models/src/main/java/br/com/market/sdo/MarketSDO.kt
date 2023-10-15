package br.com.market.sdo

import br.com.market.sdo.base.BaseSDO

data class MarketSDO(
    override var localId: String,
    override var active: Boolean = true,
    var id: Long? = null,
    var address: AddressSDO? = null,
    var name: String? = null,
    var companyId: Long? = null,
): BaseSDO()