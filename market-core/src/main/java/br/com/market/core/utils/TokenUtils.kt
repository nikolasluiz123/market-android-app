package br.com.market.core.utils

/**
 * Objeto utilitário que possui funções de uso estático para manipular
 * o token de acesso ao serviço.
 *
 * @author Nikolas Luiz Schmitt
 */
object TokenUtils {

    /**
     * Função utilizada para formatar o token de acesso, de forma
     * que possa ser interpretado corretamente pelo serviço.
     *
     * @param token Token gerado ao logar.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun formatToken(token: String) = "Bearer $token"
}