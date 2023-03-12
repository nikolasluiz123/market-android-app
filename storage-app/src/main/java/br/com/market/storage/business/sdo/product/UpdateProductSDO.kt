package br.com.market.storage.business.sdo.product

/**
 * Classe para solicitar uma atualização do produto no serviço.
 *
 * @property localProductId Id do produto na base local.
 * @property name Nome do produto.
 * @property imageUrl Link da imagem que será baixada na exibição.
 *
 * @author Nikolas Luiz Schmitt
 */
data class UpdateProductSDO(
    var localProductId: Long? = null,
    var name: String,
    var imageUrl: String = ""
)