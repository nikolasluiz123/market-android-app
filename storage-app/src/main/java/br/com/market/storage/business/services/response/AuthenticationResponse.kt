package br.com.market.storage.business.services.response

data class AuthenticationResponse(
    var token: String? = null,
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null
): IMarketServiceResponse
