package br.com.market.storage.ui.viewmodels.brand

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.core.extensions.navParamToString
import br.com.market.localdataaccess.tuples.ProductImageTuple
import br.com.market.storage.R
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.brand.BrandUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrandViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<BrandUIState> = MutableStateFlow(BrandUIState())
    val uiState get() = _uiState.asStateFlow()

    private var categoryId = savedStateHandle.get<String>(argumentCategoryId)
    private var brandId = savedStateHandle.get<String>(argumentBrandId)

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onBrandNameChange = {
                    _uiState.value = _uiState.value.copy(brandName = it)
                },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.brandName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            brandNameErrorMessage = context.getString(R.string.category_screen_category_name_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            brandNameErrorMessage = ""
                        )
                    }

                    isValid
                }
            )
        }

        categoryId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val categoryDomain = categoryRepository.findById(id)

                _uiState.update { currentState ->
                    currentState.copy(
                        categoryDomain = categoryDomain
                    )
                }
            }
        }

        brandId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val brandDomain = brandRepository.findById(id)

                _uiState.update { currentState ->
                    currentState.copy(
                        brandDomain = brandDomain,
                        brandName = brandDomain.name
                    )
                }
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                products = getProducts()
            )
        }
    }

    private fun getProducts(): Flow<PagingData<ProductImageTuple>> {
        val categoryId = categoryId?.navParamToString()
        val brandId = brandId?.navParamToString()

        return if (categoryId != null && brandId != null) {
            productRepository.findProducts(categoryId = categoryId, brandId = brandId)
        } else {
            emptyFlow()
        }
    }

    fun saveBrand() {
        _uiState.value.brandDomain?.let { brandDomain ->
            viewModelScope.launch {
                brandRepository.save(_uiState.value.categoryDomain?.id!!, brandDomain)

                _uiState.update { currentState ->
                    val domain = currentState.brandDomain
                    currentState.copy(brandDomain = domain?.copy(active = domain.active))
                }
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
                    brandName = brandDomain.name
                )
            }
        }
    }
}