package br.com.market.sdo

import br.com.market.sdo.base.BaseSDO

/**
 * Classe para envio e recebimento de marcado do serviço.
 *
 * @property localId Id gerado pela base local da categoria
 * @property marketId Id da empresa
 * @property active Flag que indica se está ativo ou não
 * @property name Nome da categoria
 *
 * @author Nikolas Luiz Schmitt
 */
data class CategorySDO(
    override var localId: String,
    override var marketId: Long? = null,
    override var active: Boolean = true,
    var name: String? = null
): BaseSDO()