package br.com.market.domain

import br.com.market.domain.base.BaseDomain

data class DeviceDomain(
    override var id: String? = null,
    override var active: Boolean = true,
    override var companyId: Long? = null,
    override var synchronized: Boolean = false,
    var name: String? = null
): BaseDomain() {
}