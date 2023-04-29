package br.com.market.storage.ui.viewmodels.brand

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.lovs.argumentBrandIdLovCallback
import br.com.market.storage.ui.states.brand.BrandLovUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BrandLovViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<BrandLovUIState> = MutableStateFlow(BrandLovUIState())
    val uiState get() = _uiState.asStateFlow()

    private var categoryId = savedStateHandle.get<String>(argumentCategoryId)

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    categoryName = categoryRepository.findById(UUID.fromString(categoryId?.navParamToString()!!)).name,
                    brands = brandRepository.findBrands(UUID.fromString(categoryId?.navParamToString()!!))
                )
            }
        }
    }

    fun addBrandLovCallback(brandId: UUID) {
        savedStateHandle[argumentBrandIdLovCallback] = brandId
    }
}