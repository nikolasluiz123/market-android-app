package br.com.market.storage.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.ui.states.CategorySearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySearchViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<CategorySearchUIState> = MutableStateFlow(CategorySearchUIState.Loading)
    val uiState get() = _uiState.asStateFlow()

    init {
        findCategories()
    }

    fun findCategories() {
        _uiState.update { CategorySearchUIState.Loading }

        viewModelScope.launch {
            delay(2000)
            categoryRepository.findCategories().collect { categories ->
                _uiState.update { CategorySearchUIState.Success(categories = categories) }
            }
        }
    }
}