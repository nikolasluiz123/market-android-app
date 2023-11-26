package br.com.market.servicedataaccess.responses.types

import java.net.HttpURLConnection

/**
 * Classe que representa uma resposta de persistência,
 * por ser utilizada, por exemplo, como retorno de uma função
 * que salva ou atualiza uma entidade.
 *
 * Ela tem como objetivo conter as chaves primárias de uma entidade
 * para que, se for necessário, possamos fazer operações com esses dados,
 * por exemplo, realizar alguma busca ou outra operação que necessite do
 * ID.
 *
 * @author Nikolas Luiz Schmitt
 */
data class PersistenceResponse(
    override var code: Int,
    override var success: Boolean = false,
    override var error: String? = null
): IMarketServiceResponse {

    fun getObjectSynchronized() = success && code != HttpURLConnection.HTTP_UNAVAILABLE
}
