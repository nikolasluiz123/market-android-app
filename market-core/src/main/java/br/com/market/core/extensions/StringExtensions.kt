package br.com.market.core.extensions

import com.google.gson.Gson
import java.text.DecimalFormat

/**
 * Função para converter um ID enviado por navegação para um Long.
 *
 * É uma implementação para não nullables.
 *
 * @return O ID convertido para String ou null se for "null".
 * @receiver A string contendo o ID a ser convertido.
 * @author Nikolas Luiz Schmitt
 */
fun String?.navParamToString(): String? {
    val value = this?.replace("}", "")?.replace("{", "")
    return if (value == "null") null else value
}

/**
 * Função que pode ser utilizada para converter uma string
 * em um valor decimal no padrão do locale.
 *
 * @return O valor decimal convertido para Double ou null se a conversão falhar.
 * @receiver A string contendo o valor decimal a ser convertido.
 * @author Nikolas Luiz Schmitt
 */
fun String.parseToDouble(): Double? {
    val formatter = DecimalFormat.getInstance()
    return formatter.parse(this)?.toDouble()
}

/**
 * Formata uma string removendo os caracteres "{" e "}".
 *
 * @return A string formatada.
 * @receiver A string a ser formatada.
 */
fun String.formatJsonNavParam() = this.substring(1, this.length - 1)

/**
 * Converte uma string JSON em um objeto do tipo especificado.
 *
 * @param clazz A classe do tipo de objeto para conversão.
 * @param gson O objeto Gson a ser usado para a conversão.
 * @return O objeto do tipo especificado.
 * @receiver A string JSON a ser convertida.
 */
fun <ARG> String.fromJsonNavParamToArgs(clazz: Class<ARG>, gson: Gson = Gson()): ARG {
    return gson.getAdapter(clazz).fromJson(this.formatJsonNavParam())
}