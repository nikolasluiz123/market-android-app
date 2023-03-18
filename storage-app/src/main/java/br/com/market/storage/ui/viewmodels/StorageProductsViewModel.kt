package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.domain.ProductDomain
import br.com.market.servicedataaccess.responses.MarketServiceResponse
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.UserRepository
import br.com.market.storage.ui.states.StorageProductsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel da tela de listagem dos Produtos do estoque.
 *
 * @property productRepository Classe responsável por realizar operações referentes ao Produto.
 * @property userRepository Classe responsável por realizar operações referentes ao Usuário.
 *
 * @author Nikolas Luiz Schmitt
 */
@HiltViewModel
class StorageProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<StorageProductsUiState> = MutableStateFlow(StorageProductsUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onToggleMessageDialog = { errorMessage ->
                    _uiState.value = _uiState.value.copy(
                        showDialogMessage = !_uiState.value.showDialogMessage,
                        serverMessage = errorMessage
                    )
                },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onSearchChange = ::onSearchProducts,
                onToggleSearch = {
                    _uiState.value = _uiState.value.copy(openSearch = !_uiState.value.openSearch)
                }
            )
        }

        viewModelScope.launch {
            productRepository.findActiveAllProducts().collect { products ->
                _uiState.value = _uiState.value.copy(
                    products = products
                )
            }
        }
    }

    /**
     * Função para realizar sincronizar os dados referentes aos Produtos.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun syncProducts(): MarketServiceResponse {
        return productRepository.syncProducts()
    }

    suspend fun syncBrands(): MarketServiceResponse {
        return brandRepository.syncBrands()
    }

    private fun onSearchProducts(searchedText: String) {
        if (searchedText.isEmpty()) {
            viewModelScope.launch {
                productRepository.findActiveAllProducts().collect { products ->
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