package br.com.market.sdo.base

/**
 * Classe base contendo os atributos comuns nos SDOs
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BaseSDO {
    abstract var localId: String
    abstract var companyId: Long?
    abstract var active: Boolean
}