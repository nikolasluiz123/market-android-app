package br.com.market.storage.ui.states

import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductBrandDomain

data class FormProductUiState(
    val productId: Long? = null,
    val productName: String = "",
    val productImage: String = "",
    val brandName: String = "",
    val brandQtd: String = "",
    val openSearch: Boolean = false,
    val brands: List<ProductBrandDomain> = mutableListOf(),
    val searchText: String = "",
    val productImageErrorMessage: String = "",
    val productNameErrorMessage: String = "",
    val brandNameErrorMessage: String = "",
    val quantidadeBrandErrorMessage: String = "",
    val openBrandDialog: Boolean = false,
    val onValidateBrand: () -> Boolean = { true },
    val onToggleBrandDialog: () -> Unit = { },
    val onValidateProduct: () -> Boolean = { true },
    val onProductNameChange: (String) -> Unit = { },
    val onProductImageChange: (String) -> Unit = { },
    val onBrandsChange: (ProductBrandDomain) -> Unit = { },
    val onBrandNameChange: (String) -> Unit = { },
    val onBrandQtdChange: (String) -> Unit = { },
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { }
)
