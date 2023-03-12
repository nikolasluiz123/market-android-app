package br.com.market.storage.business.sdo.brand

/**
 * Classe usada para solicitação da inclusão de uma Marca no serviço.
 *
 * @property localProductId Id do produto na base local.
 * @property localBrandId Id da marca na base local.
 * @property name nome da marca.
 * @property count quantidade do produto de uma marca específica.
 *
 * @author Nikolas Luiz Schmitt
 */
data class NewBrandSDO(
    var localProductId: Long? = null,
    var localBrandId: Long? = null,
    var name: String,
    var count: Int = 0
)
