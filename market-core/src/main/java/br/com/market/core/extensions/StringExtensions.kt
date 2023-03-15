package br.com.market.core.extensions

/**
 * Função para converter um ID enviado por navegação para um Long.
 *
 * É uma implementação para não nullables.
 *
 * @author Nikolas Luiz Schmitt
 */
fun String.navParamToString(): String = this.replace("}", "").replace("{", "")