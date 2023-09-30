package br.com.market.core.inputs.formatter

import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.format
import java.time.LocalDateTime

/**
 * Implementação da interface IAdvancedFilterFormatter que formata um intervalo de datas e horas avançado.
 *
 * @constructor Cria uma instância de DateTimeRangeAdvancedFilterFormatter.
 * @author Nikolas Luiz Schmitt
 */
class DateTimeRangeFormatter : IFormatter {

    /**
     * Formata um intervalo de datas e horas (par de LocalDateTime) em uma string.
     *
     * @param value O valor a ser formatado, deve ser um par de LocalDateTime.
     * @return A string formatada resultante ou null se o valor for nulo.
     * @throws ClassCastException Se o valor não for um par de LocalDateTime.
     */
    @Suppress("UNCHECKED_CAST")
    override fun formatToString(value: Any?): String? {
        value as Pair<LocalDateTime?, LocalDateTime?>?

        return when {
            value?.first != null && value?.second != null -> {
                "${value!!.first!!.format(EnumDateTimePatterns.DATE_TIME)} - ${value.second!!.format(EnumDateTimePatterns.DATE_TIME)}"
            }

            value?.first != null -> {
                value!!.first!!.format(EnumDateTimePatterns.DATE_TIME)
            }

            value?.second != null -> {
                value!!.second!!.format(EnumDateTimePatterns.DATE_TIME)
            }

            else -> null
        }
    }
}