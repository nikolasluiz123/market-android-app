package br.com.market.storage.ui.states.brand

import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain

data class BrandUIState(
    var brandDomain: BrandDomain? = null,
    val brandName: String = "",
    val onBrandNameChange: (String) -> Unit = { },
    val brandNameErrorMessage: String = "",
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState