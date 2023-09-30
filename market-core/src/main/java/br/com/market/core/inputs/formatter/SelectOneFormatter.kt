package br.com.market.core.inputs.formatter

/**
 * Implementação da interface [IFormatter] para formatação avançada de seleção única.
 *
 * Esta classe realiza a formatação de um valor que é um par (Pair) contendo uma string e um inteiro,
 * retornando somente a primeira parte (a string) para representação em texto formatada adequada para
 * ser usada em filtros avançados.
 *
 * @constructor Cria uma instância de [SelectOneFormatter].
 * @author Nikolas Luiz Schmitt
 */
class SelectOneFormatter : IFormatter {

    /**
     * Converte um valor do tipo [Pair] (contendo uma string e um inteiro) em uma representação de texto
     * formatada para uso em filtros avançados.
     *
     * @param value O valor a ser formatado, deve ser um [Pair] contendo uma string e um inteiro.
     * @return A primeira parte do par (Pair) como uma representação formatada em texto, ou null se o valor for nulo.
     */
    @Suppress("UNCHECKED_CAST")
    override fun formatToString(value: Any?): String? {
        return (value as Pair<String, Int>?)?.first
    }
}
