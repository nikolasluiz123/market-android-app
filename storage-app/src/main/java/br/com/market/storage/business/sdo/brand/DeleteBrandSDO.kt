package br.com.market.storage.business.sdo.brand

/**
 * Classe usada para solicitar um Delete de uma Marca ao serviço.
 *
 * @property localBrandId Id da Marca da base local.
 *
 * @author Nikolas Luiz Schmitt
 */
data class DeleteBrandSDO(
    var localBrandId: Long
)
