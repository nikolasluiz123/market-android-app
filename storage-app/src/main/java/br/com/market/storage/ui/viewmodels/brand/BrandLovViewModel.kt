package br.com.market.storage.ui.viewmodels.brand

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.states.brand.BrandLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrandLovViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<BrandLovUIState> = MutableStateFlow(BrandLovUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    brands = brandRepository.findBrands()
                )
            }
        }
    }

//    fun addBrandLovCallback(brandId: UUID) {
//        savedStateHandle[argumentBrandIdLovCallback] = brandId
//    }
}