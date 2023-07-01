package br.com.market.sdo

data class CompanySDO(
    var name: String,
    var themeDefinitionsDTO: ThemeDefinitionsSDO,
    var id: Long? = null,
    var active: Boolean = true
)
