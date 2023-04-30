package br.com.market.storage.ui.viewmodels.brand

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.states.brand.BrandLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BrandLovViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<BrandLovUIState> = MutableStateFlow(BrandLovUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        findBrands()
    }

    fun findBrands(brandName: String?  = null) {
        _uiState.update {
            it.copy(
                brands = brandRepository.findBrands(brandName = brandName)
            )
        }
    }
}