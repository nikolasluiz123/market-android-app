package br.com.market.commerce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.commerce.repository.ProductRepository
import br.com.market.commerce.ui.state.ProductsUIState
import br.com.market.localdataaccess.tuples.ProductImageTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductsUIState> = MutableStateFlow(ProductsUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(products = findProducts())
            }
        }
    }

    private fun findProducts(): Flow<PagingData<ProductImageTuple>> {
        return repository.findProducts().flow
    }

}