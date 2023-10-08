package br.com.market.commerce.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.commerce.ui.screens.product.ProductsScreen
import br.com.market.commerce.ui.viewmodel.ProductsViewModel

internal const val productsScreenRoute = "productsScreenRoute"

fun NavGraphBuilder.productsScreen(
    onBackClick: () -> Unit,
) {
    composable(route = productsScreenRoute) {
        val viewModel = hiltViewModel<ProductsViewModel>()

        ProductsScreen(
            viewModel = viewModel,
        )
    }
}

fun NavController.navigateToProductsScreen(navOptions: NavOptions? = null) {
    navigate(route = productsScreenRoute, navOptions = navOptions)
}