package br.com.market.storage.ui.states.category

import br.com.market.domain.CategoryDomain

sealed class CategoryUIState {

    object Loading : CategoryUIState()

    object Failure : CategoryUIState()

    data class Success(
        var categoryDomain: CategoryDomain? = null,
        val categoryName: String = "",
        val onCategoryNameChange: (String) -> Unit = { },
        val categoryNameErrorMessage: String = ""
    ) : CategoryUIState()
}