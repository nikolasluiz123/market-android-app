package br.com.market.sdo

data class DeviceSDO(
    var id: String? = null,
    var active: Boolean = true,
    var name: String? = null,
    var companyId: Long? = null
)
