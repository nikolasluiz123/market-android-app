package br.com.market.storage.ui.states

import br.com.market.storage.ui.domains.ProductDomain

data class StorageProductsUiState(
    val products: List<ProductDomain> = emptyList(),
    val searchText: String = "",
    val openSearch: Boolean = false,
    val onToggleSearch: () -> Unit = { },
    val onSearchChange: (String) -> Unit = { },
    val registersCountToSync: Long = 0,
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false }
) : BaseUiState() {

    fun isSearching(): Boolean = searchText.isNotBlank()
}