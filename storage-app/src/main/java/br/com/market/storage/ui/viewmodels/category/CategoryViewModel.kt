package br.com.market.storage.ui.viewmodels.category

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.extensions.navParamToString
import br.com.market.core.ui.states.Field
import br.com.market.domain.CategoryDomain
import br.com.market.storage.R
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.category.CategoryUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<CategoryUIState> = MutableStateFlow(CategoryUIState())
    val uiState get() = _uiState.asStateFlow()

    private var categoryId: String? = savedStateHandle[argumentCategoryId]

    init {
        _uiState.update { currentState ->
            currentState.copy(
                nameField = Field(onChange = { _uiState.value = _uiState.value.copy(nameField = _uiState.value.nameField.copy(value = it)) }),
                onShowDialog = { type, message, onConfirm, onCancel ->
                    _uiState.value = _uiState.value.copy(
                        dialogType = type,
                        showDialog = true,
                        dialogMessage = message,
                        onConfirm = onConfirm,
                        onCancel = onCancel
                    )
                },
                onHideDialog = { _uiState.value = _uiState.value.copy(showDialog = false) },
                onToggleLoading = { _uiState.value = _uiState.value.copy(showLoading = !_uiState.value.showLoading) },
                onValidate = {
                    var isValid = true

                    validateName { isValid = it }

                    isValid
                }
            )
        }

        categoryId?.navParamToString()?.let { categoryId ->

            viewModelScope.launch {
                val categoryDomain = categoryRepository.findById(categoryId)
                val brands = brandRepository.findBrands(categoryId)

                _uiState.update { currentState ->
                    currentState.copy(
                        categoryDomain = categoryDomain,
                        nameField = _uiState.value.nameField.copy(value = categoryDomain.name),
                        brands = brands
                    )
                }
            }
        }
    }

    private fun validateName(onValidChange: (Boolean) -> Unit) {
        viewModelScope.launch {
            when {
                _uiState.value.nameField.valueIsEmpty() -> {
                    onValidChange(false)

                    _uiState.value = _uiState.value.copy(
                        nameField = _uiState.value.nameField.copy(errorMessage = context.getString(R.string.category_screen_category_name_required_validation_message))
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(
                        nameField = _uiState.value.nameField.copy(errorMessage = "")
                    )
                }
            }
        }
    }

    fun saveCategory(onSuccess: () -> Unit, onError: (message: String) -> Unit) {
       _uiState.value.categoryDomain = if (_uiState.value.categoryDomain == null) {
            CategoryDomain(name = _uiState.value.nameField.value)
        } else {
           _uiState.value.categoryDomain!!.copy(name = _uiState.value.nameField.value)
        }

        viewModelScope.launch {
            val response = categoryRepository.save(_uiState.value.categoryDomain!!)

            if (response.success) {
                _uiState.update { currentState ->
                    val domain = currentState.categoryDomain
                    currentState.copy(categoryDomain = domain?.copy(active = domain.active))
                }

                onSuccess()
            } else {
                _uiState.value.categoryDomain = null
                onError(response.error ?: "")
            }
        }
    }

    fun toggleActive() {
        _uiState.value.categoryDomain?.let { domain ->
            viewModelScope.launch {
                categoryRepository.toggleActive(domain.id!!, domain.active)
            }
        }
    }

    fun updateList(simpleFilterText: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(brands = brandRepository.findBrands(currentState.categoryDomain?.id!!, simpleFilterText))
        }
    }
}