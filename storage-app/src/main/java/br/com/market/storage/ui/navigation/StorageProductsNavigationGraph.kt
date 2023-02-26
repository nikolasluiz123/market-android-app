package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.StorageProductsScreen
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel

fun NavGraphBuilder.storageProductsGraph(
    navController: NavHostController
) {
    composable(route = StorrageAppDestinations.StorageProducts.route) {
        val storageProductsViewModel = hiltViewModel<StorageProductsViewModel>()
        StorageProductsScreen(
            viewModel = storageProductsViewModel,
            onItemClick = { productId ->
                navController.navigate("${StorrageAppDestinations.FormProduct.route}?productId={$productId}")
            },
            onLogoutClick = {
                navController.inclusiveNavigation(
                    originRoute = StorrageAppDestinations.StorageProducts.route,
                    destinyRoute = StorrageAppDestinations.Login.route
                )
            },
            onFABNewProductClick = { navController.navigate(StorrageAppDestinations.FormProduct.route) }
        )
    }
}