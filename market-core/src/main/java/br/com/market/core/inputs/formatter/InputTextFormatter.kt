package br.com.market.core.inputs.formatter

/**
 * Classe base para formatação avançada de strings.
 *
 * Esta classe base implementa a interface [IFormatter] e fornece uma formatação
 * simples para valores do tipo String, removendo espaços em branco no início e no final.
 *
 * @constructor Cria uma instância de [InputTextFormatter].
 * @author Nikolas Luiz Schmitt
 */
open class InputTextFormatter : IFormatter {

    /**
     * Converte um valor do tipo String em uma representação de texto formatada para uso em filtros avançados.
     *
     * @param value O valor do tipo String a ser formatado.
     * @return A representação formatada em texto do valor, com espaços em branco no início e no final removidos,
     *         ou null se o valor for nulo.
     */
    override fun formatToString(value: Any?): String? {
        return value?.toString()?.trim()
    }
}
