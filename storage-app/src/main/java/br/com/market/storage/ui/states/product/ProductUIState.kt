package br.com.market.storage.ui.states.product

import android.net.Uri
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductDomain
import br.com.market.enums.EnumUnit

data class ProductUIState(
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

    var images: MutableList<Uri> = mutableListOf(),

    override val onValidate: () -> Boolean = { false }
): IValidationUIState
