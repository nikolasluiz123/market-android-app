package br.com.market.storage.business.sdo.brand

/**
 * Classe utilizada para solicitar uma atualização de uma marca no serviço.
 *
 * @property localBrandId Id da marca na base local.
 * @property name nome da marca.
 * @property count quantidade do produto da marca específica.
 *
 * @author Nikolas Luiz Schmitt
 */
data class UpdateBrandSDO(
    var localBrandId: Long,
    var name: String,
    var count: Int = 0
)
