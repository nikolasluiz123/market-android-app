package br.com.market.storage.ui.viewmodels.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.states.product.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductUIState> = MutableStateFlow(ProductUIState())
    val uiState get() = _uiState.asStateFlow()

    private var brandId = savedStateHandle.get<String>(argumentBrandId)
    private var productId = savedStateHandle.get<String>(argumentProductId)

    init {
        brandId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val brandDomain = brandRepository.findById(UUID.fromString(id))

                _uiState.update { currentState ->
                    currentState.copy(
                        brandDomain = brandDomain
                    )
                }
            }
        }
    }
}