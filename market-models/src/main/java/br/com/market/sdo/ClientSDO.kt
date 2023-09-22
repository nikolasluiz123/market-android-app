package br.com.market.sdo

import br.com.market.sdo.base.BaseSDO

data class ClientSDO(
    override var localId: String,
    override var active: Boolean,
    var user: UserSDO,
    var address: AddressSDO,
    var cpf: String
) : BaseSDO()