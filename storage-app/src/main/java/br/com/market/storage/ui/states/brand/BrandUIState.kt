package br.com.market.storage.ui.states.brand

import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.CategoryDomain

data class BrandUIState(
    var categoryDomain: CategoryDomain? = null,
    var brandDomain: BrandDomain? = null,
    val brandName: String = "",
    val onBrandNameChange: (String) -> Unit = { },
    val brandNameErrorMessage: String = "",
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState