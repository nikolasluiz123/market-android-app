package br.com.market.storage.ui.viewmodels.brand

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.core.extensions.navParamToString
import br.com.market.domain.ProductImageReadDomain
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
    @ApplicationContext context: Context,
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

        loadCategory {  }
        loadBrandDomain()
        loadCategoryBrandActive()
    }

    private fun loadCategory(onError: (String) -> Unit) {
        categoryId?.navParamToString()?.let { categoryId ->
            viewModelScope.launch {
                val response = categoryRepository.findById(categoryId)

                if (response.success) {
                    _uiState.update { currentState ->
                        currentState.copy(categoryDomain = response.value)
                    }
                } else {
                    withContext(Main) {
                        onError(response.error ?: "")
                    }
                }
            }
        }
    }

    private fun loadBrandDomain() {
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
    }

    private fun loadCategoryBrandActive() {
        val categoryId = categoryId?.navParamToString()
        val brandId = brandId?.navParamToString()

        if (!categoryId.isNullOrEmpty() && !brandId.isNullOrEmpty()) {
            viewModelScope.launch {
                val active = brandRepository.findCategoryBrandActive(categoryId = categoryId, brandId = brandId)
                _uiState.update { currentState -> currentState.copy(active = active) }
            }
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
                    brandName = brandDomain.name
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