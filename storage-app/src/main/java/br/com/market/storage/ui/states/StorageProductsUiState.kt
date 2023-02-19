package br.com.market.storage.ui.states

import br.com.market.storage.ui.domains.ProductDomain

data class StorageProductsUiState(
    val products: List<ProductDomain> = emptyList(),
    val searchText: String = "",
    val openSearch: Boolean = false,
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { }
) {

    fun isSearching(): Boolean = searchText.isNotBlank()
}