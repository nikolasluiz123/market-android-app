package br.com.market.domain.base

/**
 * Classe base de domínio, contendo os atributos comuns
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BaseDomain {
    abstract var id: String?
    abstract var active: Boolean
    abstract var companyId: Long?
    abstract var synchronized: Boolean
}