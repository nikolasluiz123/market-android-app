package br.com.market.storage.ui.viewmodels.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.core.extensions.navParamToString
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.product.ProductLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductLovViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
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
                products = productRepository.findProducts(
                    categoryId = categoryId.navParamToString()!!,
                    brandId = brandId.navParamToString()!!,
                    simpleFilter = simpleFilterText
                )
            )
        }
    }
}
