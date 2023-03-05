package br.com.market.storage.business.services.response

interface IMarketServiceResponse {
    var code: Int
    var success: Boolean
    var error: String?
}