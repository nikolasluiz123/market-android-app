package br.com.market.storage.ui.states.product

import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductDomain
import br.com.market.domain.ProductImageDomain
import br.com.market.enums.EnumUnit

data class ProductUIState(
    var categoryId: String? = null,
    var brandDomain: BrandDomain? = null,
    var productDomain: ProductDomain? = null,

    val productName: String = "",
    val onProductNameChange: (String) -> Unit = { },
    val productNameErrorMessage: String = "",

    val productPrice: String = "",
    val onProductPriceChange: (String) -> Unit = { },
    val productPriceErrorMessage: String = "",

    val productQuantity: String = "",
    val onProductQuantityChange: (String) -> Unit = { },
    val productQuantityErrorMessage: String = "",

    var productQuantityUnit: EnumUnit? = null,
    val productQuantityUnitErrorMessage: String = "",

    var images: MutableList<ProductImageDomain> = mutableListOf(),

    val onShowDialog: (String, String) -> Unit = { _, _ -> },
    val onHideDialog: () -> Unit = { },
    val showDialog: Boolean = false,
    val dialogTitle: String = "",
    val dialogMessage: String = "",

    override val onValidate: () -> Boolean = { false }
): IValidationUIState
