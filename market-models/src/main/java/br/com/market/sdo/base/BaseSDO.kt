package br.com.market.sdo.base

import java.util.*

/**
 * Classe base contendo os atributos comuns nos SDOs
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BaseSDO {
    abstract var localId: UUID
    abstract var companyId: Long?
    abstract var active: Boolean
}