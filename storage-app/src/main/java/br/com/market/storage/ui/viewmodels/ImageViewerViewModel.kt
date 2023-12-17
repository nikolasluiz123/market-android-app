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
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        _uiState.update { currentState ->
            currentState.copy(
                onShowDialog = { type, message, onConfirm, onCancel ->
                    _uiState.value = _uiState.value.copy(
                        dialogType = type,
                        dialogMessage = message,
                        showDialog = true,
                        onConfirm = onConfirm,
                        onCancel = onCancel
                    )
                },
                onHideDialog = { _uiState.value = _uiState.value.copy(showDialog = false) },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
            )
        }

        loadScreen { _uiState.value = _uiState.value.copy(internalErrorMessage = it)  }
    }

    private fun loadScreen(onError: (String) -> Unit) {
        productImageId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                _uiState.value.onToggleLoading()

                val productImageDomain = productRepository.findProductImageDomain(id)
                val response = productRepository.findProductByLocalId(productImageDomain?.productId!!)

                if (response.success) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productImageDomain = productImageDomain,
                            productName = response.value!!.product.name
                        )
                    }
                } else {
                    withContext(Main) { onError(response.error ?: "") }
                }

                _uiState.value.onToggleLoading()
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
