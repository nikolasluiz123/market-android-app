package br.com.market.storage.ui.navigation.category

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.category.CategorySearchScreen
import br.com.market.storage.ui.viewmodels.category.CategorySearchViewModel
import java.util.*

internal const val categorySearchScreenRoute = "categorySearch"

fun NavGraphBuilder.categorySearchScreen(
    onButtonBackClickFailureScreen: () -> Unit,
    onAddCategoryClick: () -> Unit,
    onCategoryClick: (UUID) -> Unit,
) {
    composable(route = categorySearchScreenRoute) {
        val categorySearchViewModel = hiltViewModel<CategorySearchViewModel>()

        CategorySearchScreen(
            viewModel = categorySearchViewModel,
            onButtonBackClickFailureScreen = onButtonBackClickFailureScreen,
            onAddCategoryClick = onAddCategoryClick,
            onCategoryClick = onCategoryClick
        )
    }
}

fun NavController.navigateToCategorySearchScreen(navOptions: NavOptions? = null) {
    navigate(route = categorySearchScreenRoute, navOptions = navOptions)
}