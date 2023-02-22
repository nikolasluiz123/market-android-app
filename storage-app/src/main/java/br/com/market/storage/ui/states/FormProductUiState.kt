package br.com.market.storage.ui.states

import br.com.market.storage.ui.domains.BrandDomain

data class FormProductUiState(
    val productId: Long? = null,
    val productName: String = "",
    val productImage: String = "",
    val brandName: String = "",
    val brandQtd: String = "",
    val openSearch: Boolean = false,
    val brands: List<BrandDomain> = mutableListOf(),
    val searchText: String = "",
    val productImageErrorMessage: String = "",
    val productNameErrorMessage: String = "",
    val onValidate: () -> Boolean = { true },
    val onProductNameChange: (String) -> Unit = { },
    val onProductImageChange: (String) -> Unit = { },
    val onBrandsChange: (BrandDomain) -> Unit = { },
    val onBrandNameChange: (String) -> Unit = { },
    val onBrandQtdChange: (String) -> Unit = { },
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { }
)
