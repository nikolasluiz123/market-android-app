package br.com.market.storage.ui.states.brand

import androidx.paging.PagingData
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.CategoryDomain
import br.com.market.localdataaccess.tuples.ProductImageTuple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BrandUIState(
    var categoryDomain: CategoryDomain? = null,
    var brandDomain: BrandDomain? = null,
    val brandName: String = "",
    val onBrandNameChange: (String) -> Unit = { },
    val brandNameErrorMessage: String = "",
    var products: Flow<PagingData<ProductImageTuple>> = emptyFlow(),
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState