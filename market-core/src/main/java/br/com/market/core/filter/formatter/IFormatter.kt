package br.com.market.core.filter.formatter

/**
 * Interface que define o contrato para formatação avançada de filtros.
 *
 * Esta interface permite a formatação de valores para uma representação em formato de texto,
 * adequada para ser usada em filtros avançados.
 *
 * @author Nikolas Luiz Schmitt
 */
interface IFormatter {

    /**
     * Converte o valor fornecido em uma representação de texto formatada para uso em filtros avançados.
     *
     * @param value O valor a ser formatado.
     * @return A representação formatada em texto do valor, ou null se o valor for nulo.
     */
    fun formatToString(value: Any?): String?

}
