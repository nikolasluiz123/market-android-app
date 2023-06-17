package br.com.market.storage.ui.viewmodels.movements

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.states.MovementsSearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovementsSearchViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovementsSearchUIState> = MutableStateFlow(MovementsSearchUIState())
    val uiState get() = _uiState.asStateFlow()

    private val categoryId: String? = savedStateHandle[argumentCategoryId]
    private val brandId: String? = savedStateHandle[argumentBrandId]
    private val productId: String? = savedStateHandle[argumentProductId]

    init {
        _uiState.update { currentState ->
            currentState.copy(
                categoryId = categoryId.navParamToString(),
                brandId = brandId.navParamToString(),
                productId = productId.navParamToString()
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
}