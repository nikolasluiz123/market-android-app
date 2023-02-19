package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.business.repository.ProductRepository
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.ui.states.StorageProductsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageProductsViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<StorageProductsUiState> = MutableStateFlow(StorageProductsUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onSearchChange = ::onSearchProducts,
                onToggleSearch = {
                    _uiState.value = _uiState.value.copy(openSearch = !_uiState.value.openSearch)
                }
            )
        }

        viewModelScope.launch {
            productRepository.findAllProducts().collect { products ->
                _uiState.value = _uiState.value.copy(
                    products = products
                )
            }
        }
    }

    private fun onSearchProducts(searchedText: String) {
        if (searchedText.isEmpty()) {
            viewModelScope.launch {
                productRepository.findAllProducts().collect { products ->
                    _uiState.value = _uiState.value.copy(
                        searchText = searchedText,
                        products = products
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(
                searchText = searchedText,
                products = _uiState.value.products.filter {
                    containsProductName(it, searchedText)
                }
            )
        }
    }

    private fun containsProductName(product: ProductDomain, searchedText: String): Boolean {
        return product.name.contains(searchedText, ignoreCase = true)
    }
}