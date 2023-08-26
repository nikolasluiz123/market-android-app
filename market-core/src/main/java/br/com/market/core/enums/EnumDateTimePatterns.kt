package br.com.market.core.enums

/**
 * Enumeração que define padrões de formato para datas e horas.
 *
 * Esta enumeração fornece padrões de formato para formatar datas e horas em strings.
 *
 * @property pattern O padrão de formato para datas e horas.
 * @constructor Cria uma instância da enumeração com o padrão de formato fornecido.
 * @author Nikolas Luiz Schmitt
 */
enum class EnumDateTimePatterns(val pattern: String) {

    /**
     * Padrão de formato para datas no formato "dd/MM/yyyy".
     */
    DATE("dd/MM/yyyy"),

    /**
     * Padrão de formato para horas no formato "HH:mm".
     */
    TIME("HH:mm"),

    /**
     * Padrão de formato para datas e horas no formato "dd/MM/yyyy HH:mm".
     */
    DATE_TIME("dd/MM/yyyy HH:mm")
}