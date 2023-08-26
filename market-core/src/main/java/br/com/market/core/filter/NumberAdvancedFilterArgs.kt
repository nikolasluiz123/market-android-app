package br.com.market.core.filter

/**
 * Argumentos para um filtro avançado de números.
 *
 * Esta classe define os argumentos para um filtro avançado de números, incluindo se o número é inteiro ou não,
 * um recurso de ID de título e um valor associado ao filtro.
 *
 * @param integer Indica se o valor do filtro é um número inteiro.
 * @param titleResId O ID do recurso de string que representa o título do filtro.
 * @param value O valor associado ao filtro.
 * @constructor Cria uma instância de [NumberAdvancedFilterArgs] com os atributos fornecidos.
 * @property integer Indica se o valor do filtro é um número inteiro.
 * @property titleResId O ID do recurso de string que representa o título do filtro.
 * @property value O valor associado ao filtro.
 */
class NumberAdvancedFilterArgs(
    val integer: Boolean,
    titleResId: Int,
    value: String = ""
) : AdvancedFilterArgs(titleResId, value)