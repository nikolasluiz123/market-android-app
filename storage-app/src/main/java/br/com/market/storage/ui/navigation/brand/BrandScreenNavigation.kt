package br.com.market.storage.ui.navigation.brand

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.screens.brand.BrandScreen
import br.com.market.storage.ui.viewmodels.brand.BrandViewModel

internal const val brandScreenRoute = "brand"
internal const val argumentBrandId = "brandId"

fun NavGraphBuilder.brandScreen(
    onBackClick: () -> Unit,
    onNavToBrandLov: (String, (String) -> Unit) -> Unit,
    onFabAddProductClick: (String, String) -> Unit,
    onProductClick: (String, String, String) -> Unit,
    onStorageButtonClick: (String, String) -> Unit
) {
    composable(route = "$brandScreenRoute?$argumentCategoryId={$argumentCategoryId}&$argumentBrandId={$argumentBrandId}") {
        val brandViewModel = hiltViewModel<BrandViewModel>()

        BrandScreen(
            viewModel = brandViewModel,
            onBackClick = onBackClick,
            onNavToBrandLov = onNavToBrandLov,
            onFabAddProductClick = onFabAddProductClick,
            onProductClick = onProductClick,
            onStorageButtonClick = onStorageButtonClick
        )
    }
}

fun NavController.navigateToBrandScreen(categoryId: String, brandId: String, navOptions: NavOptions? = null) {
    navigate(route = "$brandScreenRoute?$argumentCategoryId={$categoryId}&$argumentBrandId={$brandId}", navOptions = navOptions)
}

fun NavController.navigateToBrandScreen(categoryId: String, navOptions: NavOptions? = null) {
    navigate(route = "$brandScreenRoute?$argumentCategoryId={$categoryId}", navOptions = navOptions)
}