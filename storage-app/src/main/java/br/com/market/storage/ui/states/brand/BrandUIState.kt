package br.com.market.storage.ui.states.brand

import androidx.paging.PagingData
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.CategoryDomain
import br.com.market.domain.ProductImageReadDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BrandUIState(
    var categoryDomain: CategoryDomain? = null,
    var brandDomain: BrandDomain? = null,
    var active: Boolean = true,
    val brandName: String = "",
    val onBrandNameChange: (String) -> Unit = { },
    val brandNameErrorMessage: String = "",
    var products: Flow<PagingData<ProductImageReadDomain>> = emptyFlow(),
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState