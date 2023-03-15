package br.com.market.domain

import java.util.*

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
    var id: UUID? = null,
    var name: String = "",
    var imageUrl: String = ""
)