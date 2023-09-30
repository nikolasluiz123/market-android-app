package br.com.market.core.inputs.arguments

import java.time.LocalDateTime

/**
 * Argumentos para um filtro avançado de datas.
 *
 * Esta classe define os argumentos para um filtro avançado de datas, incluindo um recurso de ID de título
 * e um valor associado ao filtro que é um par de datas [LocalDateTime].
 *
 * @param titleResId O ID do recurso de string que representa o título do filtro.
 * @param value O valor associado ao filtro, um par de [LocalDateTime] representando o intervalo de datas.
 * @constructor Cria uma instância de [DateTimeRangeInputArgs] com o ID do recurso de título e o valor associado.
 * @property titleResId O ID do recurso de string que representa o título do filtro.
 * @property value O valor associado ao filtro, um par de [LocalDateTime] representando o intervalo de datas.
 */
open class DateTimeRangeInputArgs(
    val titleResId: Int,
    val value: Pair<LocalDateTime?, LocalDateTime?>? = null
)
