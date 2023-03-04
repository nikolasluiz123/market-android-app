package br.com.market.storage.business.sdo.user

data class RegisterRequestSDO(
    var name: String = "",
    var email: String = "",
    var password: String = ""
)