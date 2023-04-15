package br.com.market.storage.ui.viewmodels.category

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.storage.R
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.category.CategoryUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    @ApplicationContext context: Context,
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
                    val successState = _uiState.value as CategoryUIState.Success

                    _uiState.value = successState.copy(categoryName = it)
                },
                onValidate = {
                    val successState = _uiState.value as CategoryUIState.Success

                    var isValid = true

                    if (successState.categoryName.isBlank()) {
                        isValid = false

                        _uiState.value = successState.copy(
                            categoryNameErrorMessage = context.getString(R.string.category_screen_category_name_required_validation_message)
                        )
                    } else {
                        _uiState.value = successState.copy(
                            categoryNameErrorMessage = ""
                        )
                    }

                    isValid
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

    fun toggleActive(active: Boolean) {
        (_uiState.value as CategoryUIState.Success).categoryDomain?.id?.let { id ->
            viewModelScope.launch {
                categoryRepository.toggleActive(id, active)
            }
        }
    }
}