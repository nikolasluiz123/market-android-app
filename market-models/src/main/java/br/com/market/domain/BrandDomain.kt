package br.com.market.domain

import java.util.*

/**
 * Classe de domínio da Brand.
 *
 * @property id Identificador.
 * @property name Nome da marca.
 * @property count Quantidade de um produto de uma marca específica.
 *
 * @author Nikolas Luiz Schmitt
 */
data class BrandDomain(
    var id: UUID? = null,
    var name: String = "",
    var count: Int = 0
)