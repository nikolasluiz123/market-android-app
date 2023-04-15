package br.com.market.storage.ui.viewmodels.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.category.CategorySearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategorySearchViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CategorySearchUIState> = MutableStateFlow(CategorySearchUIState.Loading)
    val uiState get() = _uiState.asStateFlow()

    init {
        findCategories()
    }

    fun findCategories() {
        _uiState.update { CategorySearchUIState.Loading }

        viewModelScope.launch {
            categoryRepository.findCategories().collect { categories ->
                _uiState.update { CategorySearchUIState.Success(categories = categories) }
            }
        }
    }
}