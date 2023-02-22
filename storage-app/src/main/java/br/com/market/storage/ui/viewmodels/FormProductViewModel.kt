package br.com.market.storage.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.business.repository.ProductRepository
import br.com.market.storage.extensions.toLongNavParam
import br.com.market.storage.ui.domains.ProductDomain
import br.com.market.storage.ui.states.FormProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FormProductUiState> = MutableStateFlow(FormProductUiState())
    val uiState get() = _uiState.asStateFlow()

    private val productId = savedStateHandle.get<String>("productId")

    init {
        updateFormInfos(productId)

        Log.i("TAG", "lero: $productId")

        _uiState.update { currentState ->
            currentState.copy(
                onProductNameChange = { _uiState.value = _uiState.value.copy(productName = it) },
                onProductImageChange = { _uiState.value = _uiState.value.copy(productImage = it) },
                onBrandNameChange = { _uiState.value = _uiState.value.copy(brandName = it) },
                onBrandQtdChange = { _uiState.value = _uiState.value.copy(brandQtd = it) },
                onBrandsChange = { _uiState.value = _uiState.value.copy(brands = _uiState.value.brands + it) },
                onToggleSearch = { _uiState.value = _uiState.value.copy(openSearch = !_uiState.value.openSearch) },
                onSearchChange = { _uiState.value = _uiState.value.copy(searchText = it) },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.productName.isBlank()) {
                        isValid = false
                        _uiState.value = _uiState.value.copy(productNameErrorMessage = "Nome do Produto é Obrigatório.")
                    }

                    if (_uiState.value.productImage.isBlank()) {
                        isValid = false
                        _uiState.value = _uiState.value.copy(productImageErrorMessage = "Link da Imagem do Produto é Obrigatório.")
                    }

                    isValid
                }
            )
        }
    }

    fun saveProduct(productDomain: ProductDomain) {
        viewModelScope.launch {
            productRepository.save(productDomain)
        }
    }

    private fun updateFormInfos(productId: String?) {
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

    fun deleteProduct(id: Long?) {
        viewModelScope.launch {
            productRepository.deleteProduct(id)
        }
    }
}
