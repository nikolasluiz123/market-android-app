package br.com.market.storage.business.services

import br.com.market.storage.business.services.response.MarketServiceResponse
import br.com.market.storage.business.services.response.PersistenceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header

interface IBaseService<N, U, D> {

    suspend fun save(@Header("Authorization") token: String, @Body sdo: N): Response<PersistenceResponse>

    suspend fun update(@Header("Authorization") token: String, @Body sdo: U): Response<PersistenceResponse>

    suspend fun delete(@Header("Authorization") token: String, @Body sdo: D): Response<MarketServiceResponse>
}