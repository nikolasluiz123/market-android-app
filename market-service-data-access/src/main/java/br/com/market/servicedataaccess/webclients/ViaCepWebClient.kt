package br.com.market.servicedataaccess.webclients

import android.content.Context
import br.com.market.domain.AddressDomain
import br.com.market.servicedataaccess.responses.extensions.getSingleValueResponseBody
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import br.com.market.servicedataaccess.services.IViaCepService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ViaCepWebClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: IViaCepService
): BaseWebClient(context) {

    suspend fun getAddressByCep(cep: String): SingleValueResponse<AddressDomain> {
        return singleValueServiceErrorHandlingBlock(
            codeBlock = {
                val response = service.getAddressByCep(cep).getSingleValueResponseBody()

                val value: AddressDomain? = response.value?.run {
                    AddressDomain(
                        cep = cep,
                        state = uf ?: "",
                        city = localidade ?: "",
                        publicPlace = logradouro ?: "",
                        number = "",
                        complement = complemento
                    )
                }

                SingleValueResponse(
                    code = response.code,
                    success = response.success,
                    error = response.error,
                    value = value
                )
            }
        )
    }
}