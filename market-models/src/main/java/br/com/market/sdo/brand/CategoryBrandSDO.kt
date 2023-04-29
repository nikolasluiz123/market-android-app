package br.com.market.sdo.brand

import br.com.market.sdo.base.BaseSDO
import java.util.*

data class CategoryBrandSDO(
    override var localId: UUID,
    override var companyId: Long? = null,
    override var active: Boolean = true,
    var localCategoryId: UUID,
    var localBrandId: UUID
): BaseSDO()