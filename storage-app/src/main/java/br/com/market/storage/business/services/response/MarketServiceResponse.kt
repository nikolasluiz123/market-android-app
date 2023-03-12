package br.com.market.storage.business.services.response

/**
 * Essa classe é a implementação apenas do mais essencial em uma resposta
 * venda do serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
data class MarketServiceResponse(
    override var code: Int,
    override var success: Boolean = false,
    override var error: String? = null
) : IMarketServiceResponse