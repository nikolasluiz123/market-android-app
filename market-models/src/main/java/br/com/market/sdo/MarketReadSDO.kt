package br.com.market.sdo

import br.com.market.sdo.base.BaseSDO

data class MarketReadSDO(
    override var localId: String,
    override var active: Boolean,
    var id: Long? = null,
    var name: String? = null,
    var companyId: Long? = null,
    var address: AddressSDO,
    var company: CompanySDO
) : BaseSDO()