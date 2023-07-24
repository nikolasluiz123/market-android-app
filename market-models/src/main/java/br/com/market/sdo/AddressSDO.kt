package br.com.market.sdo

data class AddressSDO(
    var localId: String,
    var active: Boolean = true,
    var state: String? = null,
    var city: String? = null,
    var publicPlace: String? = null,
    var number: String? = null,
    var complement: String? = null,
    var cep: String? = null
)
