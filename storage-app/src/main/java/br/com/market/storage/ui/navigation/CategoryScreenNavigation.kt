package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.category.CategorySearchScreen
import br.com.market.storage.ui.viewmodels.CategorySearchViewModel

internal const val categoryScreenRoute = "category"


fun NavGraphBuilder.categoryScreen(
    onButtonBackClickFailureScreen: () -> Unit,
) {
    composable(route = categoryScreenRoute) {
        val categorySearchViewModel = hiltViewModel<CategorySearchViewModel>()

        CategorySearchScreen(
            viewModel = categorySearchViewModel,
            onButtonBackClickFailureScreen = onButtonBackClickFailureScreen,
        )
    }
}

fun NavController.navigateToCategoryScreen(navOptions: NavOptions? = null) {
    navigate(route = categoryScreenRoute, navOptions = navOptions)
}