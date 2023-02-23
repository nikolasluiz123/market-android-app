package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.business.repository.BrandRepository
import br.com.market.storage.business.repository.ProductRepository
import br.com.market.storage.extensions.toLongNavParam
import br.com.market.storage.ui.domains.BrandDomain
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
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FormProductUiState> = MutableStateFlow(FormProductUiState())
    val uiState get() = _uiState.asStateFlow()

    private val productId = savedStateHandle.get<String>("productId")

    init {
        updateProductFormInfos(productId)
        updateProductBrandsInfos(productId)

        _uiState.update { currentState ->
            currentState.copy(
                onProductNameChange = { _uiState.value = _uiState.value.copy(productName = it, productNameErrorMessage = "") },
                onProductImageChange = { _uiState.value = _uiState.value.copy(productImage = it, productImageErrorMessage = "") },
                onBrandNameChange = { _uiState.value = _uiState.value.copy(brandName = it) },
                onBrandQtdChange = { _uiState.value = _uiState.value.copy(brandQtd = it) },
                onBrandsChange = { _uiState.value = _uiState.value.copy(brands = _uiState.value.brands + it) },
                onToggleSearch = { _uiState.value = _uiState.value.copy(openSearch = !_uiState.value.openSearch) },
                onSearchChange = { _uiState.value = _uiState.value.copy(searchText = it) },
                onToggleBrandDialog = { _uiState.value = _uiState.value.copy(openBrandDialog = !_uiState.value.openBrandDialog) },
                onValidateProduct = {
                    var isValid = true

                    if (_uiState.value.productName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = "Nome do Produto é Obrigatório."
                        )
                    }

                    if (_uiState.value.productImage.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productImageErrorMessage = "Link da Imagem do Produto é Obrigatório."
                        )
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

    private fun updateProductFormInfos(productId: String?) {
        if (productId != null) {
            val productDomainFlow = productRepository.findProductById(productId.toLongNavParam())

            viewModelScope.launch {
                productDomainFlow.collect {
                    _uiState.value = _uiState.value.copy(
                        productId = it?.id,
                        productName = it?.name ?: "",
                        productImage = it?.imageUrl ?: ""
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(
                productId = null,
                productName = "",
                productImage = ""
            )
        }
    }

    private fun updateProductBrandsInfos(productId: String?) {
        if (productId != null) {
            val productBrandDomainFlow = brandRepository.findProductBrandsByProductId(productId.toLongNavParam()!!)

            viewModelScope.launch {
                productBrandDomainFlow.collect {
                    _uiState.value = _uiState.value.copy(brands = it)
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(brands = emptyList())
        }
    }

    fun deleteProduct(id: Long?) {
        viewModelScope.launch {
            productRepository.deleteProduct(id)
        }
    }

    fun saveBrand(productId: Long?, brand: BrandDomain) {
        productId?.let {
            viewModelScope.launch {
                brandRepository.saveBrand(productId, brand)
            }
        }
    }
}
