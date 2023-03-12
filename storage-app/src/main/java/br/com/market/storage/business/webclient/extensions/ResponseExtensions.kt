package br.com.market.storage.business.webclient.extensions

import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

fun Response<PersistenceResponse>.getPersistenceResponseBody(): PersistenceResponse {
    val type = object : TypeToken<PersistenceResponse>() {}.type
    return this.body() ?: Gson().fromJson(this.errorBody()!!.charStream(), type)
}

fun Response<MarketServiceResponse>.getResponseBody(): MarketServiceResponse {
    val type = object : TypeToken<MarketServiceResponse>() {}.type
    return this.body() ?: Gson().fromJson(this.errorBody()!!.charStream(), type)
}