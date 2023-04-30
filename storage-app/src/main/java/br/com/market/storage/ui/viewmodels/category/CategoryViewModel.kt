package br.com.market.storage.ui.viewmodels.category

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.storage.R
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.brand.BrandRepository
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
    private val brandRepository: BrandRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CategoryUIState> = MutableStateFlow(CategoryUIState())
    val uiState get() = _uiState.asStateFlow()

    private var categoryId = savedStateHandle.get<String>(argumentCategoryId)

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onCategoryNameChange = {
                    _uiState.value = _uiState.value.copy(categoryName = it)
                },
                onValidate = {
                    var isValid = true

                    if (_uiState.value.categoryName.isBlank()) {
                        isValid = false

                        _uiState.value = _uiState.value.copy(
                            categoryNameErrorMessage = context.getString(R.string.category_screen_category_name_required_validation_message)
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            categoryNameErrorMessage = ""
                        )
                    }

                    isValid
                }
            )
        }

        categoryId?.navParamToString()?.let {
            val categoryId = UUID.fromString(it)

            viewModelScope.launch {
                val categoryDomain = categoryRepository.findById(categoryId)
                val brands = brandRepository.findBrands(categoryId)

                _uiState.update { currentState ->
                    currentState.copy(
                        categoryDomain = categoryDomain,
                        categoryName = categoryDomain.name,
                        brands = brands
                    )
                }
            }
        }
    }

    fun saveCategory() {
        _uiState.value.categoryDomain?.let { categoryDomain ->
            viewModelScope.launch {
                categoryRepository.save(categoryDomain)

                _uiState.update { currentState ->
                    val domain = currentState.categoryDomain
                    currentState.copy(categoryDomain = domain?.copy(active = domain.active))
                }
            }
        }
    }

    fun toggleActive() {
        _uiState.value.categoryDomain?.id?.let { id ->
            viewModelScope.launch {
                categoryRepository.toggleActive(id)
            }
        }
    }
}