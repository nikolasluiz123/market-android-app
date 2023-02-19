package br.com.market.storage.ui.states

import br.com.market.storage.ui.transferobjects.TOBrand

data class FormProductUiState(
    val productName: String = "",
    val productImage: String = "",
    val brandName: String = "",
    val brandQtd: String = "",
    val openSearch: Boolean = false,
    val brands: List<TOBrand> = mutableListOf(),
    val searchText: String = "",
    val onProductNameChange: (String) -> Unit = { },
    val onProductImageChange: (String) -> Unit = { },
    val onBrandsChange: (TOBrand) -> Unit = { },
    val onBrandNameChange: (String) -> Unit = { },
    val onBrandQtdChange: (String) -> Unit = { },
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { }
)
