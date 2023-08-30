package br.com.market.storage.ui.viewmodels.category

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.market.core.preferences.PreferencesKey
import br.com.market.core.preferences.dataStore
import br.com.market.storage.repository.CategoryRepository
import br.com.market.storage.repository.ProductRepository
import br.com.market.storage.repository.StorageOperationsHistoryRepository
import br.com.market.storage.repository.UserRepository
import br.com.market.storage.repository.brand.BrandRepository
import br.com.market.storage.ui.states.category.CategorySearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val storageOperationsHistoryRepository: StorageOperationsHistoryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<CategorySearchUIState> = MutableStateFlow(CategorySearchUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(categories = categoryRepository.findCategories())
        }
    }

    fun sync(onFinish: () -> Unit) {
        viewModelScope.launch {
            userRepository.sync()
            categoryRepository.sync()
            brandRepository.sync()
            productRepository.sync()
            storageOperationsHistoryRepository.sync()
        }.invokeOnCompletion {
            onFinish()
        }
    }

    fun logout() {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKey.TOKEN] = ""
                preferences[PreferencesKey.USER] = ""
            }
        }
    }

    fun updateList(simpleFilterText: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(categories = categoryRepository.findCategories(simpleFilterText))
        }
    }
}