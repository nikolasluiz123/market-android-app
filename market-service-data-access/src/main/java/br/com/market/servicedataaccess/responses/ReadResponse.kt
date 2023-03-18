package br.com.market.servicedataaccess.responses

class ReadResponse<DTO>(
    var values: List<DTO> = emptyList(),
    override var code: Int,
    override var success: Boolean,
    override var error: String? = null
) : IMarketServiceResponse