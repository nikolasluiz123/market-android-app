package br.com.market.storage.ui.states.product

import android.net.Uri
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductDomain

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

    val productUnit: String = "",
    val onProductUnitChange: (String) -> Unit = { },
    val productUnitErrorMessage: String = "",

    var images: MutableList<Uri> = mutableListOf()
)
