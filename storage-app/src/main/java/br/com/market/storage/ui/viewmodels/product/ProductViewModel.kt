package br.com.market.storage.ui.viewmodels.product

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.extensions.format
import br.com.market.core.extensions.navParamToString
import br.com.market.core.inputs.formatter.InputNumberFormatter
import br.com.market.core.ui.states.Field
import br.com.market.domain.ProductDomain
import br.com.market.domain.ProductImageDomain
import br.com.market.enums.EnumUnit
import br.com.market.sdo.ProductAndReferencesSDO
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import br.com.market.storage.R
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.states.product.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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
                name = Field(onChange = { _uiState.value = _uiState.value.copy(name = _uiState.value.name.copy(value = it)) }),
                price = Field(onChange = { _uiState.value = _uiState.value.copy(price = _uiState.value.price.copy(value = it)) }),
                quantity = Field(onChange = { _uiState.value = _uiState.value.copy(quantity = _uiState.value.quantity.copy(value = it)) }),
                quantityUnit = Field(onChange = {
                    _uiState.value = _uiState.value.copy(quantityUnit = _uiState.value.quantityUnit.copy(value = it))
                }),
                onShowDialog = { type, message, onConfirm, onCancel ->
                    _uiState.value = _uiState.value.copy(
                        dialogType = type,
                        dialogMessage = message,
                        showDialog = true,
                        onConfirm = onConfirm,
                        onCancel = onCancel
                    )
                },
                onHideDialog = { _uiState.value = _uiState.value.copy(showDialog = false) },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onValidate = {
                    var isValid = true

                    validateImages { isValid = it }
                    validateName { isValid = it }
                    validatePrice { isValid = it }
                    validateQuantity { isValid = it }
                    validateQuantityUnit { isValid = it }

                    isValid
                }
            )
        }

        loadScreen { _uiState.value = _uiState.value.copy(internalErrorMessage = it) }
    }

    private fun loadScreen(onError: (String) -> Unit) {
        val categoryId = categoryId?.navParamToString()
        val brandId = brandId?.navParamToString()
        val productId = productId?.navParamToString()

        viewModelScope.launch {
            withContext(Main) { _uiState.value.onToggleLoading() }

            loadBrand(categoryId = categoryId, brandId = brandId) {
                loadProduct(productId = productId)
            }

            withContext(Main) { _uiState.value.onToggleLoading() }
        }
    }

    private suspend fun loadBrand(
        categoryId: String?,
        brandId: String?,
        onSuccess: suspend () -> Unit
    ) {
        if (!categoryId.isNullOrEmpty() && !brandId.isNullOrEmpty()) {
            val categoryBrand = brandRepository.cacheFindCategoryBrandBy(categoryId = categoryId, brandId = brandId)!!
            val brandDomain = brandRepository.cacheFindBrandDomainById(brandId = brandId)

            _uiState.update { currentState ->
                currentState.copy(
                    brandDomain = brandDomain,
                    categoryBrandId = categoryBrand.id
                )
            }

            onSuccess()
        }
    }

    private suspend fun loadProduct(productId: String?) {
        productId?.let { id ->
            val productDomain = productRepository.cacheFindById(id)

            _uiState.update { currentState ->
                currentState.copy(
                    productDomain = productDomain,
                    name = _uiState.value.name.copy(value = productDomain.name!!),
                    price = _uiState.value.price.copy(value = productDomain.price!!.format()),
                    quantity = _uiState.value.quantity.copy(value = productDomain.quantity!!.format()),
                    quantityUnit = _uiState.value.quantityUnit.copy(value = context.getString(productDomain.quantityUnit!!.labelResId)),
                    images = productDomain.images.toMutableStateList()
                )
            }
        }
    }

    private fun validateImages(onValidChange: (Boolean) -> Unit) {
        if (_uiState.value.images.size == 0) {
            onValidChange(false)

            _uiState.value.onShowDialog?.onShow(
                type = EnumDialogType.ERROR,
                message = context.getString(R.string.product_screen_required_photo_validation_message),
                onConfirm = {},
                onCancel = {}
            )
        }
    }

    private fun validateName(onValidChange: (Boolean) -> Unit) {
        when {
            _uiState.value.name.valueIsEmpty() -> {
                onValidChange(false)

                _uiState.value = _uiState.value.copy(
                    name = _uiState.value.name.copy(errorMessage = context.getString(R.string.product_screen_product_name_required_validation_message))
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    name = _uiState.value.name.copy(errorMessage = "")
                )
            }
        }
    }

    private fun validatePrice(onValidChange: (Boolean) -> Unit) {
        when {
            _uiState.value.price.valueIsEmpty() -> {
                onValidChange(false)

                _uiState.value = _uiState.value.copy(
                    price = _uiState.value.price.copy(errorMessage = context.getString(R.string.product_screen_product_price_required_validation_message))
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    price = _uiState.value.price.copy(errorMessage = "")
                )
            }
        }
    }

    private fun validateQuantity(onValidChange: (Boolean) -> Unit) {
        when {
            _uiState.value.quantity.valueIsEmpty() -> {
                onValidChange(false)

                _uiState.value = _uiState.value.copy(
                    quantity = _uiState.value.quantity.copy(errorMessage = context.getString(R.string.product_screen_product_quantity_required_validation_message))
                )
            }

            else -> {
                _uiState.value = _uiState.value.copy(
                    quantity = _uiState.value.quantity.copy(errorMessage = "")
                )
            }
        }
    }

    private fun validateQuantityUnit(onValidChange: (Boolean) -> Unit) {
        if (_uiState.value.quantityUnit.valueIsEmpty()) {
            onValidChange(false)

            _uiState.value = _uiState.value.copy(
                quantityUnit = _uiState.value.quantityUnit.copy(errorMessage = context.getString(R.string.product_screen_product_quantity_unity_required_validation_message))
            )
        } else {
            _uiState.value = _uiState.value.copy(
                quantityUnit = _uiState.value.quantityUnit.copy(errorMessage = "")
            )
        }
    }

    fun saveProduct(onSuccess: () -> Unit, onError: (message: String) -> Unit) {
        val unit = EnumUnit.entries.first { context.getString(it.labelResId) == _uiState.value.quantityUnit.value }
        val price = InputNumberFormatter(integer = false).formatStringToValue(_uiState.value.price.value)!!.toDouble()

        _uiState.value.productDomain = if (_uiState.value.productDomain == null) {
            ProductDomain(
                name = _uiState.value.name.value,
                price = price,
                quantity = _uiState.value.quantity.value.toDouble(),
                quantityUnit = unit,
                images = _uiState.value.images
            )
        } else {
            _uiState.value.productDomain!!.copy(
                name = _uiState.value.name.value,
                price = price,
                quantity = _uiState.value.quantity.value.toDouble(),
                quantityUnit = unit,
                images = _uiState.value.images
            )
        }

        viewModelScope.launch {
            val response = productRepository.save(
                categoryBrandId = _uiState.value.categoryBrandId!!,
                domain = _uiState.value.productDomain!!
            )

            withContext(Main) {
                if (response.success) onSuccess() else onError(response.error ?: "")
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
