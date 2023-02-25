package br.com.market.storage.ui.states

import br.com.market.storage.ui.domains.ProductBrandDomain

data class FormProductUiState(
    val productId: Long? = null,
    val productName: String = "",
    val productImage: String = "",
    val brandId: Long? = null,
    val brandName: String = "",
    val brandQtd: String = "",
    val openSearch: Boolean = false,
    val brands: List<ProductBrandDomain> = mutableListOf(),
    val searchText: String = "",
    val productImageErrorMessage: String = "",
    val productNameErrorMessage: String = "",
    val brandNameErrorMessage: String = "",
    val qtdBrandErrorMessage: String = "",
    val openBrandDialog: Boolean = false,
    val onValidateBrand: () -> Boolean = { true },
    val onHideBrandDialog: () -> Unit = { },
    val onShowBrandDialog: (ProductBrandDomain?) -> Unit = { },
    val onValidateProduct: () -> Boolean = { true },
    val onProductNameChange: (String) -> Unit = { },
    val onProductImageChange: (String) -> Unit = { },
    val onBrandsChange: (ProductBrandDomain) -> Unit = { },
    val onBrandNameChange: (String) -> Unit = { },
    val onBrandQtdChange: (String) -> Unit = { },
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { }
)
