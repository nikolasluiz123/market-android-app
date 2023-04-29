package br.com.market.storage.ui.states.category

import androidx.paging.PagingData
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.CategoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CategoryUIState(
    var categoryDomain: CategoryDomain? = null,
    val categoryName: String = "",
    val onCategoryNameChange: (String) -> Unit = { },
    val categoryNameErrorMessage: String = "",
    override val onValidate: () -> Boolean = { false },
    var brands: Flow<PagingData<BrandDomain>> = emptyFlow()
) : IValidationUIState