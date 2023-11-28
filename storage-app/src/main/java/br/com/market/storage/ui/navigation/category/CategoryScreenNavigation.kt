package br.com.market.storage.ui.navigation.category

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.callbacks.ITextInputNavigationCallback
import br.com.market.storage.ui.screens.category.CategoryScreen
import br.com.market.storage.ui.viewmodels.category.CategoryViewModel


internal const val categoryScreenRoute = "category"
internal const val argumentCategoryId = "categoryId"

fun NavGraphBuilder.categoryScreen(
    onBackClick: () -> Unit,
    onFabAddBrandClick: (String) -> Unit,
    onBrandItemClick: (String, String) -> Unit,
    textInputCallback: ITextInputNavigationCallback
) {
    composable(route = "$categoryScreenRoute?$argumentCategoryId={$argumentCategoryId}") {
        val categoryViewModel = hiltViewModel<CategoryViewModel>()

        CategoryScreen(
            viewModel = categoryViewModel,
            onBackClick = onBackClick,
            onFabAddBrandClick = onFabAddBrandClick,
            onBrandItemClick = onBrandItemClick,
            textInputCallback = textInputCallback
        )
    }
}

fun NavController.navigateToCategoryScreen(categoryId: String, navOptions: NavOptions? = null) {
    navigate(route = "$categoryScreenRoute?$argumentCategoryId={$categoryId}", navOptions = navOptions)
}

fun NavController.navigateToCategoryScreen(navOptions: NavOptions? = null) {
    navigate(route = categoryScreenRoute, navOptions = navOptions)
}