package br.com.market.storage.ui.viewmodels.movements

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.core.extensions.navParamToString
import br.com.market.core.filter.MovementFilters
import br.com.market.domain.StorageOperationHistoryReadDomain
import br.com.market.market.common.viewmodel.BaseSearchViewModel
import br.com.market.market.pdf.generator.reports.StorageOperationsReportGenerator
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.StorageOperationsHistoryRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.states.MovementsSearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovementsSearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
    private val storageOperationsHistoryRepository: StorageOperationsHistoryRepository,
    private val reportGenerator: StorageOperationsReportGenerator
) : BaseSearchViewModel<StorageOperationHistoryReadDomain, MovementFilters>() {

    private val _uiState: MutableStateFlow<MovementsSearchUIState> = MutableStateFlow(MovementsSearchUIState())
    val uiState get() = _uiState.asStateFlow()

    private val categoryId: String? = savedStateHandle[argumentCategoryId]
    private val brandId: String? = savedStateHandle[argumentBrandId]
    private val productId: String? = savedStateHandle[argumentProductId]

    init {
        filter = MovementFilters(
            categoryId = categoryId.navParamToString()!!,
            brandId = brandId.navParamToString()!!,
            productId = productId.navParamToString()
        )

        _uiState.update { currentState ->
            currentState.copy(
                categoryId = categoryId.navParamToString(),
                brandId = brandId.navParamToString(),
                productId = productId.navParamToString(),
                operations = getDataFlow(filter),
            )
        }

        loadScreen()
    }

    private fun loadScreen() {
        val brandId = brandId.navParamToString()
        val productId = productId.navParamToString()

        viewModelScope.launch {
            loadBrand(brandId = brandId) {
                loadProduct(productId = productId)
            }
        }
    }

    private suspend fun loadBrand(brandId: String?, onSuccess: suspend () -> Unit) {
        if (!brandId.isNullOrEmpty()) {
            val name = brandRepository.cacheFindById(brandId).name
            _uiState.update { currentState -> currentState.copy(brandName = name) }
            onSuccess()
        }
    }

    private suspend fun loadProduct(productId: String?) {
        if (!productId.isNullOrEmpty()) {
            val product = productRepository.cacheFindById(productId = productId)

            _uiState.update { currentState ->
                currentState.copy(
                    productName = product.name,
                    productQuantity = storageOperationsHistoryRepository.findProductStorageQuantity(productId)
                )
            }
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

    fun generateReport(onStart: () -> Unit, onFinish: () -> Unit) {
        viewModelScope.launch {
            withContext(Main) {
                onStart()
            }

            reportGenerator.generateReport()

            withContext(Main) {
                onFinish()
            }
        }
    }

    fun onAdvancedFilterScreenCallback(filters: MovementFilters) {
        filter = filters

        _uiState.update {
            it.copy(operations = getDataFlow(filter))
        }
    }

    override fun onSimpleFilterChange(value: String?) {
        filter.quickFilter = value
        _uiState.value = _uiState.value.copy(operations = getDataFlow(filter))
    }

    override fun getDataFlow(filter: MovementFilters): Flow<PagingData<StorageOperationHistoryReadDomain>> {
        return storageOperationsHistoryRepository.getConfiguredPager(context, filter).flow
    }
}