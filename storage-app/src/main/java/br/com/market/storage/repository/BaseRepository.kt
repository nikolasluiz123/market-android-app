package br.com.market.storage.repository

import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse

/**
 * Clase base dos repositories que contem as implementações comuns.
 *
 * @author Nikolas Luiz Schmitt
 */
open class BaseRepository {

    /**
     * Função para realizar a busca de informações do serviço rest e persistir
     * na base local do dispositivo.
     *
     * @param T Tipo do DTO retornado pelo serviço.
     * @param onWebServiceFind Função para a execução da pesquisa específica do serviço.
     * @param onPersistData Função para a execução da persistência específica na base local.
     *
     * @author Nikolas Luiz Schmitt
     */
    protected suspend fun <T> importPagingData(
        onWebServiceFind: suspend (Int, Int) -> ReadResponse<T>,
        onPersistData: suspend (List<T>) -> Unit
    ): MarketServiceResponse {
        var offset = 0
        lateinit var response: ReadResponse<T>

        do {
            response = onWebServiceFind(DEFAULT_IMPORT_PAGE_SIZE, offset)

            if (!response.success) {
                return response.toBaseResponse()
            }

            onPersistData(response.values)

            offset += DEFAULT_IMPORT_PAGE_SIZE

        } while (response.values.isNotEmpty())

        return response.toBaseResponse()
    }

    companion object {
        protected const val DEFAULT_IMPORT_PAGE_SIZE = 50
    }
}