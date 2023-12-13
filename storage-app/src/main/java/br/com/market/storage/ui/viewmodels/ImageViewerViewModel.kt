package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.domain.ProductImageDomain
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.ui.navigation.argumentProductImageId
import br.com.market.storage.ui.states.ImageViewerUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<ImageViewerUIState> = MutableStateFlow(ImageViewerUIState())

    val uiState get() = _uiState.asStateFlow()
    private var productImageId = savedStateHandle.get<String>(argumentProductImageId)

    init {
        productImageId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val productImageDomain = productRepository.findProductImageDomain(id)
                val productDomain = productRepository.findProductByLocalId(productImageDomain?.productId!!)

                _uiState.update { currentState ->
                    currentState.copy(
                        productImageDomain = productImageDomain,
                        productName = productDomain.name
                    )
                }
            }
        }
    }

    fun save(productImageDomain: ProductImageDomain) {
        viewModelScope.launch {
            productRepository.updateImage(productImageDomain)
        }
    }

    fun toggleImageActive() {
        viewModelScope.launch {
            productRepository.toggleActiveProductImage(
                imageId = _uiState.value.productImageDomain?.id!!,
                productId = _uiState.value.productImageDomain?.productId!!
            )
        }
    }
}
