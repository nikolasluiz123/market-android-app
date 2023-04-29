package br.com.market.sdo.brand

import br.com.market.sdo.base.BaseSDO
import java.util.*

/**
 * Classe para envio e recebimento de marcado do serviço.
 *
 * @property localId Id gerado pela base local da marca
 * @property companyId Id da empresa
 * @property active Flag que indica se está ativo ou não
 * @property name Nome da marca
 *
 * @author Nikolas Luiz Schmitt
 */
data class BrandSDO(
    override var localId: UUID,
    override var companyId: Long? = null,
    override var active: Boolean = true,
    var name: String? = null
): BaseSDO()