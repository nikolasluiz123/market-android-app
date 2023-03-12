package br.com.market.storage.ui.domains

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
    var id: Long? = null,
    var name: String = "",
    var count: Int = 0
)