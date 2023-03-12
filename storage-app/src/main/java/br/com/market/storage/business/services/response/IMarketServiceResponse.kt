package br.com.market.storage.business.services.response

/**
 * Interface de resposta do serviço que define os atributos
 * essenciais para a resposta de qualquer execução.
 *
 * @property code O código HTTP referente a resposta
 * @property success Uma flag que indica se foi bem sucedida a requisição.
 * @property error Caso um erro tenha ocorrido, teremos a mensagem nessa propriedade.
 *
 * @author Nikolas Luiz Schmitt
 */
interface IMarketServiceResponse {
    var code: Int
    var success: Boolean
    var error: String?
}