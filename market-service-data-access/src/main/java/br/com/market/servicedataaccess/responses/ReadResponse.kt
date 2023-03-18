package br.com.market.servicedataaccess.responses

/**
 * Classe que representa uma resposta de leitura, utilizada normalmente
 * em retorno de consultas.
 *
 * Tem como objetivo armazenar a lista retornada juntamente com informações
 * padrões de respostas do serviço.
 *
 * @param DTO Tipo da classe de retorno da consulta.
 *
 * @property values Lista com os valores consultados.
 *
 * @author Nikolas Luiz Schmitt
 */
class ReadResponse<DTO>(
    var values: List<DTO> = emptyList(),
    override var code: Int,
    override var success: Boolean,
    override var error: String? = null
) : IMarketServiceResponse