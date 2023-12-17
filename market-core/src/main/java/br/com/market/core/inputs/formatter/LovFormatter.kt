package br.com.market.core.inputs.formatter

/**
 * Implementação da interface [IFormatter] que formata um par (Pair) para um filtro avançado.
 *
 * Esta classe realiza a formatação de um valor que é um par (Pair) em uma representação de texto formatada,
 * adequada para ser usada em filtros avançados.
 *
 * @constructor Cria uma instância de [LovFormatter].
 * @author Nikolas Luiz Schmitt
 */
class LovFormatter : IFormatter<Pair<String, String?>?> {

    /**
     * Converte um valor do tipo [Pair] em uma representação de texto formatada para uso em filtros avançados.
     *
     * @param value O valor a ser formatado, deve ser um [Pair] contendo dois elementos.
     * @return A segunda parte do par (Pair) como uma representação formatada em texto, ou null se o valor for nulo.
     */
    override fun formatToString(value: Pair<String, String?>?): String? = value?.second
    override fun formatStringToValue(formatedValue: String?): Pair<String, String>? {
        TODO("Not yet implemented")
    }
}