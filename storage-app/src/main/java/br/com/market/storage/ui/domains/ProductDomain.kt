package br.com.market.storage.ui.domains

/**
 * Classe de domínio que representa um Product
 *
 * @property id Identificador do produto.
 * @property name Nome do produto.
 * @property imageUrl Link da imagem do produto.
 *
 * @author Nikolas Luiz Schmitt
 */
data class ProductDomain(
    var id: Long? = null,
    var name: String = "",
    var imageUrl: String = ""
)