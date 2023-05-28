package br.com.market.sdo.brand

import br.com.market.sdo.base.BaseSDO

data class CategoryBrandSDO(
    override var localId: String,
    override var companyId: Long? = null,
    override var active: Boolean = true,
    var localCategoryId: String,
    var localBrandId: String
): BaseSDO()