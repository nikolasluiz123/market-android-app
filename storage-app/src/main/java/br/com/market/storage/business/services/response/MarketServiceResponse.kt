package br.com.market.storage.business.services.response

data class MarketServiceResponse(
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null
) : IMarketServiceResponse