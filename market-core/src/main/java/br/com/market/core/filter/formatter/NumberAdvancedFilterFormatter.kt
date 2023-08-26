package br.com.market.core.filter.formatter

import java.text.DecimalFormat

/**
 * Implementação da interface [IAdvancedFilterFormatter] para formatação avançada de números.
 *
 * Esta classe permite formatar valores numéricos em uma representação de texto formatada,
 * adequada para ser usada em filtros avançados.
 *
 * @constructor Cria uma instância de [NumberAdvancedFilterFormatter].
 * @param integer Indica se o valor deve ser formatado como um número inteiro.
 * @param minFractionDigits O número mínimo de casas decimais a serem exibidas.
 * @param maxFractionDigits O número máximo de casas decimais a serem exibidas.
 * @author Nikolas Luiz Schmitt
 */
class NumberAdvancedFilterFormatter(
    private val integer: Boolean,
    private val minFractionDigits: Int = 2,
    private val maxFractionDigits: Int = 2
) : IAdvancedFilterFormatter {

    /**
     * Converte um valor numérico em uma representação de texto formatada para uso em filtros avançados.
     *
     * @param value O valor numérico a ser formatado.
     * @return A representação formatada em texto do valor numérico, ou null se o valor for nulo.
     */
    override fun formatToString(value: Any?): String? {
        return if (integer) {
            (value as Long?)?.toString()
        } else {
            (value as Double?)?.let {
                DecimalFormat.getNumberInstance().run {
                    minimumFractionDigits = minFractionDigits
                    maximumFractionDigits = maxFractionDigits
                    format(it)
                }
            }
        }
    }
}
