package br.com.market.storage.ui.states.category

import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.CategoryDomain

data class CategoryUIState(
    var categoryDomain: CategoryDomain? = null,
    val categoryName: String = "",
    val onCategoryNameChange: (String) -> Unit = { },
    val categoryNameErrorMessage: String = "",
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState