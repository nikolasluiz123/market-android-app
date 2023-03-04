package br.com.market.storage.business.sdo.user

data class AuthenticationRequestSDO(
    var email: String = "",
    var password: String = ""
)