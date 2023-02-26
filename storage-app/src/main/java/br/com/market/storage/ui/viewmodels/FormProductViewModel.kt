package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.business.repository.BrandRepository
import br.com.market.storage.business.repository.ProductRepository
import br.com.market.storage.extensions.toLongNavParam
import br.com.market.storage.ui.domains.BrandDomain
import br.com.market.storage.ui.domains.ProductBrandDomain
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

    private var productId = savedStateHandle.get<String>("productId")

    init {
        updateProductFormInfos()
        updateProductBrandsInfos()

        _uiState.update { currentState ->
            currentState.copy(
                onProductNameChange = { _uiState.value = _uiState.value.copy(productName = it, productNameErrorMessage = "") },
                onProductImageChange = { _uiState.value = _uiState.value.copy(productImage = it, productImageErrorMessage = "") },
                onBrandNameChange = { _uiState.value = _uiState.value.copy(brandName = it) },
                onBrandQtdChange = { _uiState.value = _uiState.value.copy(brandQtd = it) },
                onBrandsChange = { _uiState.value = _uiState.value.copy(brands = _uiState.value.brands + it) },
                onToggleSearch = { _uiState.value = _uiState.value.copy(openSearch = !_uiState.value.openSearch) },
                onSearchChange = ::updateProductBrandsInfos,
                onHideBrandDialog = { _uiState.value = _uiState.value.copy(openBrandDialog = false) },
                onShowBrandDialog = { productBrandDomain ->
                    _uiState.value = _uiState.value.copy(
                        brandId = productBrandDomain?.brandId,
                        brandName = productBrandDomain?.brandName ?: "",
                        brandQtd = productBrandDomain?.count?.toString() ?: "",
                        openBrandDialog = true
                    )
                },
                onValidateProduct = {
                    var isValid = true

                    if (_uiState.value.productName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = "O nome do Produto é obrigatório."
                        )
                    }

                    if (_uiState.value.productImage.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productImageErrorMessage = "O link da imagem do Produto é obrigatório."
                        )
                    }

                    isValid
                },
                onValidateBrand = {
                    var isValid = true

                    if (_uiState.value.brandName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            brandNameErrorMessage = "O nome da Marca é obrigatório."
                        )
                    }

                    if (_uiState.value.brandQtd.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            qtdBrandErrorMessage = "A quantidade da Marca é obrigatória."
                        )
                    } else if (_uiState.value.brandQtd.toInt() <= 0) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            qtdBrandErrorMessage = "A quantidade da Marca é inválida."
                        )
                    }

                    isValid
                }
            )
        }
    }

    fun saveProduct(productDomain: ProductDomain) {
        viewModelScope.launch {
            productId = productRepository.save(productDomain).toString()
            updateProductBrandsInfos()
        }
    }

    fun deleteProduct() {
        productId?.let {
            viewModelScope.launch {
                productRepository.deleteProduct(it.toLongNavParam())
            }
        }
    }

    fun deleteBrand(brandId: Long) {
        viewModelScope.launch {
            productRepository.deleteBrandAndReferences(brandId)
        }
    }

    fun saveBrand(brand: BrandDomain) {
        productId?.let {
            viewModelScope.launch {
                brandRepository.saveBrand(it.toLongNavParam(), brand)
            }
        }
    }

    fun permissionNavToBrand(): Boolean {
        return productId != null
    }

    private fun updateProductFormInfos() {
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

    private fun updateProductBrandsInfos(searchedText: String = "") {
        if (productId != null) {
            val productBrandDomainFlow = brandRepository.findProductBrandsByProductId(productId.toLongNavParam())

            viewModelScope.launch {
                productBrandDomainFlow.collect { productBrandDomainList ->
                    val brands = if (searchedText.isNotEmpty()) {
                        productBrandDomainList.filter { productBrandDomain ->
                            containsProductNameOrBrandName(productBrandDomain, searchedText)
                        }
                    } else {
                        productBrandDomainList
                    }

                    _uiState.value = _uiState.value.copy(
                        searchText = searchedText,
                        brands = brands
                    )
                }
            }
        }
    }

    private fun containsProductNameOrBrandName(product: ProductBrandDomain, searchedText: String): Boolean {
        return product.productName.contains(searchedText, ignoreCase = true) ||
                product.brandName.contains(searchedText, ignoreCase = true)
    }
}
