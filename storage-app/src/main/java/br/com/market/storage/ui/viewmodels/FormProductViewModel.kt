package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.business.mappers.ProductMapper
import br.com.market.storage.business.repository.ProductRepository
import br.com.market.storage.extensions.toLongNavParam
import br.com.market.storage.ui.states.FormProductUiState
import br.com.market.storage.ui.domains.ProductDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FormProductUiState> = MutableStateFlow(FormProductUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onProductNameChange = { _uiState.value = _uiState.value.copy(productName = it) },
                onProductImageChange = { _uiState.value = _uiState.value.copy(productImage = it) },
                onBrandNameChange = { _uiState.value = _uiState.value.copy(brandName = it) },
                onBrandQtdChange = { _uiState.value = _uiState.value.copy(brandQtd = it) },
                onBrandsChange = { _uiState.value = _uiState.value.copy(brands = _uiState.value.brands + it) },
                onToggleSearch = { _uiState.value = _uiState.value.copy(openSearch = !_uiState.value.openSearch) },
                onSearchChange = { _uiState.value = _uiState.value.copy(searchText = it) }
            )
        }
    }

    fun saveProduct(productDomain: ProductDomain) {
        viewModelScope.launch {
            val product = ProductMapper.toProductModel(productDomain)
            productRepository.save(product)
        }
    }

    fun updateFormInfos(productId: String?) {
        if (productId != null) {
            val productDomainFlow = productRepository.findProductById(productId.toLongNavParam())

            viewModelScope.launch {
                productDomainFlow.collect {
                    _uiState.value = _uiState.value.copy(productId = it?.id)
                    _uiState.value = _uiState.value.copy(productName = it?.name ?: "")
                    _uiState.value = _uiState.value.copy(productImage = it?.imageUrl ?: "")
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(productId = null)
            _uiState.value = _uiState.value.copy(productName = "")
            _uiState.value = _uiState.value.copy(productImage = "")
        }
    }
}
