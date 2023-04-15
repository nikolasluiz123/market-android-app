package br.com.market.storage.ui.navigation.category

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.category.CategoryScreen
import br.com.market.storage.ui.viewmodels.category.CategoryViewModel
import java.util.*


internal const val categoryScreenRoute = "category"
internal const val argumentCategoryId = "categoryId"

fun NavGraphBuilder.categoryScreen(onBackClick: () -> Unit) {
    composable(route = "$categoryScreenRoute?$argumentCategoryId={$argumentCategoryId}") {
        val categoryViewModel = hiltViewModel<CategoryViewModel>()

        CategoryScreen(
            viewModel = categoryViewModel,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToCategoryScreen(categoryId: UUID, navOptions: NavOptions? = null) {
    navigate(route = "$categoryScreenRoute?$argumentCategoryId={$categoryId}", navOptions = navOptions)
}

fun NavController.navigateToCategoryScreen(navOptions: NavOptions? = null) {
    navigate(route = categoryScreenRoute, navOptions = navOptions)
}