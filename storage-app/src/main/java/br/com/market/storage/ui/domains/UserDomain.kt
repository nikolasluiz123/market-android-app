package br.com.market.storage.ui.domains

data class UserDomain(
    var id: Long? = null,
    var name: String = "",
    var email: String = "",
    var password: String = ""
)