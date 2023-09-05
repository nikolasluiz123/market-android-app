package br.com.market.storage.ui.viewmodels.product

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.format
import br.com.market.core.extensions.navParamToString
import br.com.market.domain.ProductImageDomain
import br.com.market.storage.R
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.states.product.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductUIState> = MutableStateFlow(ProductUIState())
    val uiState get() = _uiState.asStateFlow()

    private var categoryId: String? = savedStateHandle[argumentCategoryId]
    private var brandId: String? = savedStateHandle[argumentBrandId]
    private var productId: String? = savedStateHandle[argumentProductId]

    init {
        _uiState.update { currentState ->
            currentState.copy(
                categoryId = categoryId.navParamToString(),
                onProductNameChange = { _uiState.value = _uiState.value.copy(productName = it) },
                onProductPriceChange = { _uiState.value = _uiState.value.copy(productPrice = it) },
                onProductQuantityChange = { _uiState.value = _uiState.value.copy(productQuantity = it) },
                onShowDialog = { title, message ->
                    _uiState.value = _uiState.value.copy(dialogTitle = title, dialogMessage = message, showDialog = true)
                },
                onHideDialog = { _uiState.value = _uiState.value.copy(showDialog = false) },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.images.size == 0) {
                        isValid = false
                        _uiState.value.onShowDialog("Atenção", "O produto deve conter ao menos uma foto.")
                    }

                    if (_uiState.value.productName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = context.getString(R.string.product_screen_product_name_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = ""
                        )
                    }

                    if (_uiState.value.productPrice.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productPriceErrorMessage = context.getString(R.string.product_screen_product_price_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            productPriceErrorMessage = ""
                        )
                    }

                    if (_uiState.value.productQuantity.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productQuantityErrorMessage = context.getString(R.string.product_screen_product_quantity_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            productQuantityErrorMessage = ""
                        )
                    }

                    if (_uiState.value.productQuantityUnit == null) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productQuantityUnitErrorMessage = context.getString(R.string.product_screen_product_quantity_unity_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            productQuantityUnitErrorMessage = ""
                        )
                    }

                    isValid
                }
            )
        }

        productId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val productDomain = productRepository.findProductDomain(id)
                _uiState.update { currentState ->
                    currentState.copy(
                        productDomain = productDomain,
                        productName = productDomain.name!!,
                        productPrice = productDomain.price!!.format(),
                        productQuantity = productDomain.quantity!!.format(),
                        productQuantityUnit = productDomain.quantityUnit!!,
                        images = productDomain.images.toMutableStateList()
                    )
                }
            }
        }

        brandId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val brandDomain = brandRepository.findById(id)
                _uiState.update { currentState -> currentState.copy(brandDomain = brandDomain) }
            }
        }
    }

    fun saveProduct() {
        _uiState.value.productDomain?.let { productDomain ->
            viewModelScope.launch {
                productRepository.saveProduct(categoryId!!.navParamToString()!!, _uiState.value.brandDomain?.id!!, productDomain)

                _uiState.update { currentState ->
                    val domain = currentState.productDomain
                    currentState.copy(productDomain = domain?.copy(active = domain.active))
                }
            }
        }
    }

    fun toggleProductActive() {
        _uiState.value.productDomain?.id?.let { productId ->
            viewModelScope.launch {
                productRepository.toggleActiveProduct(productId = productId)
            }
        }
    }

    fun toggleImageActive(byteArray: ByteArray, id: String? = null) {
        _uiState.value.images.removeIf { image -> image.byteArray.contentEquals(byteArray) }

        _uiState.update { currentState ->
            currentState.copy(images = _uiState.value.images)
        }

        id?.let {
            viewModelScope.launch {
                productRepository.toggleActiveProductImage(it, _uiState.value.productDomain?.id!!)
            }
        }
    }

    fun addImage(byteArray: ByteArray) {
        val productImageDomain = ProductImageDomain(
            byteArray = byteArray,
            productId = _uiState.value.productDomain?.id,
            principal = _uiState.value.images.size == 0 // primeira imagem adicionada será a principal
        )

        _uiState.value.images.add(productImageDomain)
    }
}
