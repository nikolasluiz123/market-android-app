package br.com.market.storage.ui.states

import br.com.market.core.ui.states.IValidationUIState

data class MovementsSearchUIState(
    var categoryId: String? = null,
    var brandId: String? = null,
    var productId: String? = null,
    var productName: String? = null,
    var brandName: String? = null,
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState {

}
