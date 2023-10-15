package br.com.market.market.compose.components.lov.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.market.common.repository.lov.ProductLovRepository
import br.com.market.market.compose.components.lov.navigation.argumentBrandId
import br.com.market.market.compose.components.lov.navigation.argumentCategoryId
import br.com.market.market.compose.components.lov.state.ProductLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductLovViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProductLovRepository
): ViewModel() {

    private val categoryId: String? = savedStateHandle[argumentCategoryId]
    private val brandId: String? = savedStateHandle[argumentBrandId]

    private val _uiState: MutableStateFlow<ProductLovUIState> = MutableStateFlow(ProductLovUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        updateList()
    }

    fun updateList(simpleFilterText: String? = null) {
        _uiState.update {
            it.copy(
                products = emptyFlow()
            )
        }
    }
}
