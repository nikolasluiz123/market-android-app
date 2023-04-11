package br.com.market.storage.ui.states

import br.com.market.domain.CategoryDomain

sealed class CategorySearchUIState {

    object Loading : CategorySearchUIState()

    object Failure : CategorySearchUIState()

    class Success(val categories: List<CategoryDomain>) : CategorySearchUIState()
}