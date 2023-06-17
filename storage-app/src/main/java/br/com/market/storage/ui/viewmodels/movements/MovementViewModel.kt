package br.com.market.storage.ui.viewmodels.movements

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.enums.EnumOperationType
import br.com.market.storage.R
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.StorageOperationsHistoryRepository
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.movement.argumentMovementId
import br.com.market.storage.ui.navigation.movement.argumentOperationType
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.states.MovementUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovementViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
    private val storageOperationsHistoryRepository: StorageOperationsHistoryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovementUIState> = MutableStateFlow(MovementUIState())
    val uiState get() = _uiState.asStateFlow()

    private val categoryId: String? = savedStateHandle[argumentCategoryId]
    private val brandId: String? = savedStateHandle[argumentBrandId]
    private val productId: String? = savedStateHandle[argumentProductId]
    private val movementId: String? = savedStateHandle[argumentMovementId]
    private val operationType: String? = savedStateHandle[argumentOperationType]

    init {
        _uiState.update { currentState ->
            currentState.copy(
                productId = productId.navParamToString(),
                categoryId = categoryId.navParamToString(),
                operationType = EnumOperationType.valueOf(operationType.navParamToString()!!),
                onQuantityChange = { _uiState.value = _uiState.value.copy(quantity = it) },
                onDescriptionChange = { _uiState.value = _uiState.value.copy(description = it) },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.productDomain == null) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = context.getString(R.string.movement_screen_product_name_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            productNameErrorMessage = ""
                        )
                    }

                    if (_uiState.value.quantity.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            quantityErrorMessage = context.getString(R.string.movement_screen_quantity_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            quantityErrorMessage = ""
                        )
                    }

                    if (_uiState.value.description.isNullOrBlank() && _uiState.value.operationType == EnumOperationType.Output) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            descriptionErrorMessage = context.getString(R.string.movement_screen_description_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            descriptionErrorMessage = ""
                        )
                    }

                    if (_uiState.value.datePrevision.isNullOrBlank() && _uiState.value.operationType == EnumOperationType.ScheduledInput) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            datePrevisionErrorMessage = context.getString(R.string.movement_screen_date_prevision_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            datePrevisionErrorMessage = ""
                        )
                    }

                    if (_uiState.value.timePrevision.isNullOrBlank() && _uiState.value.operationType == EnumOperationType.ScheduledInput) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            timePrevisionErrorMessage = context.getString(R.string.movement_screen_time_prevision_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            timePrevisionErrorMessage = ""
                        )
                    }

                    isValid
                }
            )
        }

        productId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val productDomain = productRepository.findProductDomain(id)
                _uiState.update { currentState -> currentState.copy(productDomain = productDomain) }
            }
        }

        brandId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val brandDomain = brandRepository.findById(id)
                _uiState.update { currentState -> currentState.copy(brandDomain = brandDomain) }
            }
        }
    }

    fun loadProductById(productId: String) {
        viewModelScope.launch {
            val productDomain = productRepository.findProductDomain(productId)

            _uiState.update { currentState ->
                currentState.copy(
                    productDomain = productDomain,
                    productName = productDomain.name!!
                )
            }
        }
    }

    fun saveMovement() {
        _uiState.value.storageOperationsHistoryDomain?.let {
            viewModelScope.launch {
                storageOperationsHistoryRepository.save(it)
            }
        }
    }
}