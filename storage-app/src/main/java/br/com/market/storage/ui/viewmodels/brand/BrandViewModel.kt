package br.com.market.storage.ui.viewmodels.brand

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.core.extensions.navParamToString
import br.com.market.core.ui.states.Field
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductImageReadDomain
import br.com.market.sdo.BrandAndReferencesSDO
import br.com.market.servicedataaccess.responses.types.SingleValueResponse
import br.com.market.storage.R
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.brand.BrandUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BrandViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<BrandUIState> = MutableStateFlow(BrandUIState())
    val uiState get() = _uiState.asStateFlow()

    private var categoryId: String? = savedStateHandle[argumentCategoryId]
    private var brandId: String? = savedStateHandle[argumentBrandId]

    init {
        _uiState.update { currentState ->
            currentState.copy(
                products = getProducts(),
                name = Field(onChange = { _uiState.value = _uiState.value.copy(name = _uiState.value.name.copy(value = it)) }),
                onShowDialog = { type, message, onConfirm, onCancel ->
                    _uiState.value = _uiState.value.copy(
                        dialogType = type,
                        showDialog = true,
                        dialogMessage = message,
                        onConfirm = onConfirm,
                        onCancel = onCancel
                    )
                },
                onHideDialog = { _uiState.value = _uiState.value.copy(showDialog = false) },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onValidate = {
                    var isValid = true

                    validateName { isValid = it }

                    isValid
                }
            )
        }

        loadScreen { _uiState.value = _uiState.value.copy(internalErrorMessage = it) }
    }

    private fun validateName(onValidChange: (Boolean) -> Unit) {
        viewModelScope.launch {
            when {
                _uiState.value.name.valueIsEmpty() -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        name = _uiState.value.name.copy(errorMessage = context.getString(R.string.category_screen_category_name_required_validation_message))
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(
                        name = _uiState.value.name.copy(errorMessage = "")
                    )
                }
            }
        }
    }

    private fun loadScreen(onError: (String) -> Unit) {
        val categoryId = categoryId?.navParamToString()
        val brandId = brandId?.navParamToString()

        if (!categoryId.isNullOrEmpty()) {
            viewModelScope.launch {
                val findCategoryResponse = categoryRepository.findById(categoryId)

                if (findCategoryResponse.success) {
                    _uiState.update { currentState -> currentState.copy(categoryDomain = findCategoryResponse.value) }

                    if (!brandId.isNullOrEmpty()) {
                        loadBrand(categoryId, brandId, onError)
                    }
                } else {
                    withContext(Main) { onError(findCategoryResponse.error ?: "") }
                }
            }
        }
    }

    private suspend fun loadBrand(categoryId: String, brandId: String, onError: (String) -> Unit) {
        val findBrandResponse = brandRepository.findBrandAndReferencesBy(categoryId, brandId)

        if (findBrandResponse.success) {
            val brandDomain = getBrandDomainFrom(findBrandResponse)

            _uiState.update { currentState ->
                currentState.copy(
                    brandDomain = brandDomain,
                    name = _uiState.value.name.copy(value = brandDomain.name),
                    active = findBrandResponse.value!!.categoryBrand.active
                )
            }
        } else {
            withContext(Main) { onError(findBrandResponse.error ?: "") }
        }
    }

    private fun getBrandDomainFrom(response: SingleValueResponse<BrandAndReferencesSDO>): BrandDomain {
        return response.value!!.run {
            BrandDomain(
                id = brand.localId,
                active = brand.active,
                marketId = brand.marketId,
                synchronized = true,
                name = brand.name!!
            )
        }
    }

    private fun getProducts(simpleFilterText: String? = null): Flow<PagingData<ProductImageReadDomain>> {
        val categoryId = categoryId?.navParamToString()
        val brandId = brandId?.navParamToString()

        return if (categoryId != null && brandId != null) {
            productRepository.findProducts(categoryId = categoryId, brandId = brandId, simpleFilter = simpleFilterText)
        } else {
            emptyFlow()
        }
    }

    fun saveBrand() {
        _uiState.value.brandDomain = if (_uiState.value.brandDomain == null) {
            BrandDomain(name = _uiState.value.name.value)
        } else {
            _uiState.value.brandDomain!!.copy(name = _uiState.value.name.value)
        }

        _uiState.value.brandDomain?.let { brandDomain ->
            viewModelScope.launch {
                brandRepository.save(_uiState.value.categoryDomain?.id!!, brandDomain)
            }
        }
    }

    fun toggleActive() {
        _uiState.value.brandDomain?.id?.let { id ->
            viewModelScope.launch {
                brandRepository.toggleActive(brandId = id, categoryId = categoryId.navParamToString()!!)
            }
        }
    }

    fun findBrandById(brandId: String) {
        viewModelScope.launch {
            val brandDomain = brandRepository.findById(brandId)

            _uiState.update { currentState ->
                currentState.copy(
                    brandDomain = brandDomain,
                    name = _uiState.value.name.copy(value = brandDomain.name)
                )
            }
        }
    }

    fun updateList(simpleFilterText: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                products = getProducts(simpleFilterText)
            )
        }
    }
}