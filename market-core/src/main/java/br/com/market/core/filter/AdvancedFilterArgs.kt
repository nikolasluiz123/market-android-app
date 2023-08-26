package br.com.market.core.filter

/**
 * Classe base para os argumentos de um filtro avançado.
 *
 * Esta classe define os argumentos básicos para um filtro avançado, incluindo um recurso de ID de título
 * e um valor associado ao filtro.
 *
 * @param titleResId O ID do recurso de string que representa o título do filtro.
 * @param value O valor associado ao filtro. Pode ser nulo.
 * @constructor Cria uma instância de [AdvancedFilterArgs] com o ID do recurso de título e o valor associado.
 * @property titleResId O ID do recurso de string que representa o título do filtro.
 * @property value O valor associado ao filtro.
 * @author Nikolas Luiz Schmitt
 */
open class AdvancedFilterArgs(
    val titleResId: Int,
    val value: Any? = null
)
