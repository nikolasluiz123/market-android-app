package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.market.storage.business.models.Product
import br.com.market.storage.ui.states.StorageProductsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StorageProductsViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<StorageProductsUiState> = MutableStateFlow(StorageProductsUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(onSearchChange = {
                _uiState.value = _uiState.value.copy(
                    searchText = it,
                    products = searchedProducts()
                )
            })
        }
    }

    private fun searchedProducts(): List<Product> {
        return _uiState.value.products.filter(::containsProductName)
    }

    private fun containsProductName(product: Product): Boolean {
        return product.name.contains(_uiState.value.searchText, ignoreCase = true)
    }
}