package br.com.market.storage.ui.navigation.category

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.category.CategorySearchScreen
import br.com.market.storage.ui.viewmodels.category.CategorySearchViewModel

internal const val categorySearchScreenRoute = "categorySearch"

fun NavGraphBuilder.categorySearchScreen(
    onAddCategoryClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onAfterLogout: () -> Unit
) {
    composable(route = categorySearchScreenRoute) {
        val categorySearchViewModel = hiltViewModel<CategorySearchViewModel>()

        CategorySearchScreen(
            viewModel = categorySearchViewModel,
            onAddCategoryClick = onAddCategoryClick,
            onCategoryClick = onCategoryClick,
            onAfterLogout = onAfterLogout
        )
    }
}

fun NavController.navigateToCategorySearchScreen(navOptions: NavOptions? = null) {
    navigate(route = categorySearchScreenRoute, navOptions = navOptions)
}