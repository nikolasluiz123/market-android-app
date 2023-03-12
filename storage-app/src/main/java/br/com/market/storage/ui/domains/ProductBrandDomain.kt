package br.com.market.storage.ui.domains

/**
 * Classe de domínio que representa uma ProductBrand.
 *
 * @property brandId Identificador da marca.
 * @property productName Nome do produto.
 * @property brandName Nome da marca.
 * @property count Quantidade de um produto de uma marca específica.
 *
 * @author Nikolas Luiz Schmitt
 */
data class ProductBrandDomain(
    var brandId: Long? = null,
    var productName: String = "",
    var brandName: String = "",
    var count: Int = 0
)