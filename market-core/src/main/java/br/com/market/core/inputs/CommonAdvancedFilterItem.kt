package br.com.market.core.inputs

import br.com.market.core.inputs.formatter.IFormatter
import br.com.market.core.inputs.values.FilterValue

/**
 * Representação de um item de filtro avançado comum.
 *
 * Esta classe define os atributos que descrevem um item de filtro avançado, incluindo informações sobre rótulo,
 * formatação, identificador, referência a rótulos, estado de seleção, habilitação, visibilidade e valor associado.
 *
 * @param labelResId O ID do recurso de string que representa o rótulo do item de filtro.
 * @param formatter A instância de [IFormatter] que será usada para formatar o valor associado ao filtro.
 * @param identifier Uma string que identifica exclusivamente o item de filtro.
 * @param labelsReference O ID do recurso de string que pode ser usado como referência para rótulos relacionados.
 * @param checked Indica se o item de filtro está marcado (selecionado) ou não.
 * @param enabled Indica se o item de filtro está habilitado para interação.
 * @param visible Indica se o item de filtro é visível na interface do usuário.
 * @param value O valor associado ao item de filtro. Pode ser nulo.
 * @constructor Cria uma instância de [CommonAdvancedFilterItem] com os atributos fornecidos.
 * @property labelResId O ID do recurso de string que representa o rótulo do item de filtro.
 * @property formatter A instância de [IFormatter] que será usada para formatar o valor associado ao filtro.
 * @property identifier Uma string que identifica exclusivamente o item de filtro.
 * @property labelsReference O ID do recurso de string que pode ser usado como referência para rótulos relacionados.
 * @property checked Indica se o item de filtro está marcado (selecionado) ou não.
 * @property enabled Indica se o item de filtro está habilitado para interação.
 * @property visible Indica se o item de filtro é visível na interface do usuário.
 * @property value O valor associado ao item de filtro.
 */
data class CommonAdvancedFilterItem<T>(
    val labelResId: Int,
    val formatter: IFormatter<T>,
    val identifier: String,
    val labelsReference: Int? = null,
    var checked: Boolean = false,
    val enabled: Boolean = true,
    val visible: Boolean = true,
    var value: T? = null
) {

    fun toFilterValue(): FilterValue<T> = FilterValue(checked, value)
}
