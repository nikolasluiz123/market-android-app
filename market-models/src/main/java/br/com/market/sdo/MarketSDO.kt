package br.com.market.sdo

data class MarketSDO(
    var id: Long? = null,
    var active: Boolean = true,
    var address: AddressSDO? = null,
    var name: String? = null,
    var companyId: Long? = null
)