package br.com.market.storage.business.sdo.product

/**
 * Classe utilizada para solicitar a inclsão de um produto no serviço.
 *
 * @property localProductId Id do produto na base local.
 * @property name Nome do produto.
 * @property imageUrl Link da imagem que será baixada para exibição.
 *
 * @author Nikolas Luiz Schmitt
 */
data class NewProductSDO(
    var localProductId: Long? = null,
    var name: String,
    var imageUrl: String = ""
)