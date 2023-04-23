package br.com.market.storage.ui.viewmodels.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.ui.states.category.CategorySearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySearchViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<CategorySearchUIState> = MutableStateFlow(CategorySearchUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(categories = categoryRepository.findCategories())
        }
    }

    fun sync() {
        viewModelScope.launch {
            categoryRepository.sync()
        }
    }
}