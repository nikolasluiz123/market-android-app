package br.com.market.storage.ui.viewmodels.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.category.CategoryUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CategoryUIState> = MutableStateFlow(CategoryUIState.Success())
    val uiState get() = _uiState.asStateFlow()

    private var categoryId = savedStateHandle.get<String>(argumentCategoryId)

    init {
        _uiState.update { currentState ->
            currentState as CategoryUIState.Success

            currentState.copy(
                onCategoryNameChange = {
                    _uiState.value = (_uiState.value as CategoryUIState.Success).copy(categoryName = it)
                }
            )
        }

        categoryId?.navParamToString()?.let { id ->
            viewModelScope.launch {
                val categoryDomain = categoryRepository.findById(UUID.fromString(id))

                _uiState.update { currentState ->
                    currentState as CategoryUIState.Success

                    currentState.copy(
                        categoryDomain = categoryDomain,
                        categoryName = categoryDomain.name
                    )
                }
            }
        }
    }

    fun saveCategory() {
        (_uiState.value as CategoryUIState.Success).categoryDomain?.let { categoryDomain ->
            viewModelScope.launch {
                categoryRepository.saveCategory(categoryDomain)
            }
        }
    }

    fun inactivateCategory() {
        (_uiState.value as CategoryUIState.Success).categoryDomain?.id?.let { id ->
            viewModelScope.launch {
                categoryRepository.inactivateCategory(id)
            }
        }
    }
}