package br.com.market.sdo.product

import java.util.*

/**
 * Classe utilizada para solicitar a remoção de um produto do serviço.
 *
 * @property localProductId Id do produto na base local.
 *
 * @author Nikolas Luiz Schmitt
 */
data class DeleteProductSDO(
    var localProductId: UUID
)