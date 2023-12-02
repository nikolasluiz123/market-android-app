package br.com.market.storage.ui.viewmodels.category

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.market.core.extensions.navParamToString
import br.com.market.core.filter.BrandFilter
import br.com.market.core.ui.states.Field
import br.com.market.domain.BrandDomain
import br.com.market.domain.CategoryDomain
import br.com.market.market.common.repository.MarketRepository
import br.com.market.market.common.viewmodel.BaseSearchViewModel
import br.com.market.storage.R
import br.com.market.storage.repository.BrandRepository
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.states.category.CategoryUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val marketRepository: MarketRepository,
    savedStateHandle: SavedStateHandle
) : BaseSearchViewModel<BrandDomain, BrandFilter>() {

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

        loadCategory {
            _uiState.value = _uiState.value.copy(internalErrorMessage = it)
        }
    }

    private fun loadCategory(onError: (String) -> Unit) {
        categoryId?.navParamToString()?.let { categoryId ->
            viewModelScope.launch {
                _uiState.value.onToggleLoading()

                val response = categoryRepository.findById(categoryId)

                if (response.success) {
                    val marketId = marketRepository.findFirst().first()?.id!!
                    filter = BrandFilter(marketId = marketId, categoryId = categoryId)

                    _uiState.update { currentState ->
                        currentState.copy(
                            categoryDomain = response.value,
                            nameField = _uiState.value.nameField.copy(value = response.value?.name!!),
                            brands = getDataFlow(filter)
                        )
                    }
                } else {
                    withContext(Main) { onError(response.error ?: "") }
                }

                _uiState.value.onToggleLoading()
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

    fun toggleActive(onSuccess: () -> Unit, onError: (message: String) -> Unit) {
        _uiState.value.categoryDomain?.let { domain ->
            viewModelScope.launch {
                val response = categoryRepository.toggleActive(domain.id!!)

                withContext(Main) {
                    if (response.success) onSuccess() else onError(response.error ?: "")
                }
            }
        }
    }

    override fun onSimpleFilterChange(value: String?) {
        filter.simpleFilter = value

        _uiState.value = _uiState.value.copy(
            brands = getDataFlow(filter)
        )
    }

    override fun getDataFlow(filter: BrandFilter): Flow<PagingData<BrandDomain>> {
        return brandRepository.getConfiguredPager(context, filter).flow
    }
}