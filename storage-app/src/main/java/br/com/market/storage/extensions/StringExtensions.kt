package br.com.market.storage.extensions

/**
 * Função para converter um ID enviado por navegação para um Long.
 *
 * É uma implementação para nullables.
 *
 * @author Nikolas Luiz Schmitt
 */
fun String?.navParamToLong(): Long? = this?.replace("}", "")?.replace("{", "")?.toLong()

/**
 * Função para converter um ID enviado por navegação para um Long.
 *
 * É uma implementação para não nullables.
 *
 * @author Nikolas Luiz Schmitt
 */
fun String.navParamToLong(): Long = this.replace("}", "").replace("{", "").toLong()