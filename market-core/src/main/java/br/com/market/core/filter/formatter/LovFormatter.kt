package br.com.market.core.filter.formatter

/**
 * Implementação da interface [IFormatter] que formata um par (Pair) para um filtro avançado.
 *
 * Esta classe realiza a formatação de um valor que é um par (Pair) em uma representação de texto formatada,
 * adequada para ser usada em filtros avançados.
 *
 * @constructor Cria uma instância de [LovFormatter].
 * @author Nikolas Luiz Schmitt
 */
class LovFormatter : IFormatter {

    /**
     * Converte um valor do tipo [Pair] em uma representação de texto formatada para uso em filtros avançados.
     *
     * @param value O valor a ser formatado, deve ser um [Pair] contendo dois elementos.
     * @return A segunda parte do par (Pair) como uma representação formatada em texto, ou null se o valor for nulo.
     */
    @Suppress("UNCHECKED_CAST")
    override fun formatToString(value: Any?): String? = (value as Pair<String, String>?)?.second
}