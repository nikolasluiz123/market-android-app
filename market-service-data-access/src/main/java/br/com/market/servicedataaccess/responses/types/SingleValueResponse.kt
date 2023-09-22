package br.com.market.servicedataaccess.responses.types

data class SingleValueResponse<T>(
    override var code: Int,
    var value: T? = null,
    override var success: Boolean = false,
    override var error: String? = null
): IMarketServiceResponse