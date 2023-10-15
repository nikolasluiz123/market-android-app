package br.com.market.commerce.ui.viewmodel.products

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.com.market.commerce.repository.ProductRepository
import br.com.market.commerce.ui.state.ProductsUIState
import br.com.market.domain.ProductImageReadDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductsUIState> = MutableStateFlow(ProductsUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(products = findProducts())
        }
    }

    fun onSimpleFilterChange(simpleFilter: String?) {
        _uiState.value = _uiState.value.copy(products = findProducts(simpleFilter))
    }

    private fun findProducts(simpleFilter: String? = null): Flow<PagingData<ProductImageReadDomain>> {
        return repository.findProducts(simpleFilter).flow
    }

}