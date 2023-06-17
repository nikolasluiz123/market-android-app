package br.com.market.storage.ui.states

import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductDomain
import br.com.market.domain.StorageOperationsHistoryDomain
import br.com.market.enums.EnumOperationType

data class MovementUIState(
    var categoryId: String? = null,
    var productId: String? = null,
    var operationType: EnumOperationType? = null,
    var brandDomain: BrandDomain? = null,
    var productDomain: ProductDomain? = null,
    var storageOperationsHistoryDomain: StorageOperationsHistoryDomain? = null,
    var productName: String = "",
    var productNameErrorMessage: String = "",
    val quantity: String = "",
    val onQuantityChange: (String) -> Unit = { },
    val quantityErrorMessage: String = "",
    var datePrevision: String? = null,
    val datePrevisionErrorMessage: String = "",
    var timePrevision: String? = null,
    val timePrevisionErrorMessage: String = "",
    val description: String? = null,
    val onDescriptionChange: (String) -> Unit = { },
    val descriptionErrorMessage: String = "",
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState {

}
