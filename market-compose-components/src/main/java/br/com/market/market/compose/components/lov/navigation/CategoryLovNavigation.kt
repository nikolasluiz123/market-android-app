package br.com.market.market.compose.components.lov.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.market.compose.components.lov.CategoryLov
import br.com.market.market.compose.components.lov.viewmodel.CategoryLovViewModel

internal const val categoryLovRoute = "brandLov"
const val categoryLovNavResultCallbackKey = "brandLovCallbackKey"

fun NavGraphBuilder.categoryLov(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = categoryLovRoute) {
        val viewModel = hiltViewModel<CategoryLovViewModel>()

        CategoryLov(
            viewModel = viewModel,
            onItemClick = onItemClick,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToCategoryLov(callback: (String) -> Unit) {
    navigateForResult(
        key = categoryLovNavResultCallbackKey,
        route = categoryLovRoute,
        callback = callback
    )
}