package br.com.market.storage.ui.viewmodels.movements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.StorageOperationsHistoryRepository
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.states.MovementsSearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovementsSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
    private val storageOperationsHistoryRepository: StorageOperationsHistoryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovementsSearchUIState> = MutableStateFlow(MovementsSearchUIState())
    val uiState get() = _uiState.asStateFlow()

    private val categoryId: String? = savedStateHandle[argumentCategoryId]
    private val brandId: String? = savedStateHandle[argumentBrandId]
    private val productId: String? = savedStateHandle[argumentProductId]

    var filter: MovementSearchScreenFilters = MovementSearchScreenFilters()
        private set

    init {
        _uiState.update { currentState ->
            currentState.copy(
                categoryId = categoryId.navParamToString(),
                brandId = brandId.navParamToString(),
                productId = productId.navParamToString(),
                operations = storageOperationsHistoryRepository.findStorageOperationHistory(
                    productId = productId.navParamToString(),
                    categoryId = categoryId.navParamToString()!!,
                    brandId = brandId.navParamToString()!!,
                    simpleFilter = null,
                    advancedFilter = filter
                )
            )
        }

        brandId.navParamToString()?.let { brandId ->
            viewModelScope.launch {
                brandRepository.findById(brandId).apply {
                    _uiState.update { currentState ->
                        currentState.copy(brandName = name)
                    }
                }
            }
        }

        productId.navParamToString()?.let { productId ->
            viewModelScope.launch {
                productRepository.findProductDomain(productId).apply {
                    _uiState.update { currentState ->
                        currentState.copy(productName = name)
                    }
                }
            }
        }
    }

    fun updateList(advancedFilter: MovementSearchScreenFilters = MovementSearchScreenFilters(), simpleFilterText: String? = null) {
        filter = advancedFilter

        _uiState.update { currentState ->
            currentState.copy(
                operations = storageOperationsHistoryRepository.findStorageOperationHistory(
                    productId = productId.navParamToString(),
                    categoryId = categoryId.navParamToString()!!,
                    brandId = brandId.navParamToString()!!,
                    simpleFilter = simpleFilterText,
                    advancedFilter = filter
                )
            )
        }
    }

    fun hasAdvancedFilterApplied(): Boolean {
        return filter.dateRealization.isFilterApplied() ||
                filter.responsible.isFilterApplied() ||
                filter.datePrevision.isFilterApplied() ||
                filter.description.isFilterApplied() ||
                filter.operationType.isFilterApplied() ||
                filter.productName.isFilterApplied() ||
                filter.quantity.isFilterApplied()
    }
}