package br.com.market.storage.ui.states

import br.com.market.storage.business.models.Product
import br.com.market.storage.sampledata.sampleProducts

data class StorageProductsUiState(
    val products: List<Product> = sampleProducts,
    val searchText: String = "",
    val onSearchChange: (String) -> Unit = { }
) {

    fun isSearching(): Boolean = searchText.isNotBlank()
}