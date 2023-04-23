package br.com.market.domain.base

import java.util.*

/**
 * Classe base de domínio, contendo os atributos comuns
 *
 * @author Nikolas Luiz Schmitt
 */
abstract class BaseDomain {
    abstract var id: UUID?
    abstract var active: Boolean
    abstract var companyId: UUID?
    abstract var synchronized: Boolean
}