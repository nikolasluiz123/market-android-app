package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.StorageProductsScreen
import br.com.market.storage.ui.viewmodels.LoginViewModel
import br.com.market.storage.ui.viewmodels.StorageProductsViewModel

fun NavGraphBuilder.storageProductsGraph(
    navController: NavHostController
) {
    composable(route = StorageAppDestinations.StorageProducts.route) {
        val storageProductsViewModel = hiltViewModel<StorageProductsViewModel>()
        val loginViewModel = hiltViewModel<LoginViewModel>()

        StorageProductsScreen(
            viewModel = storageProductsViewModel,
            onItemClick = { productId ->
                navController.navigate("${StorageAppDestinations.FormProduct.route}?productId={$productId}")
            },
            onLogoutClick = {
                loginViewModel.logout()

                navController.inclusiveNavigation(
                    originRoute = StorageAppDestinations.StorageProducts.route,
                    destinyRoute = StorageAppDestinations.Login.route
                )
            },
            onFABNewProductClick = { navController.navigate(StorageAppDestinations.FormProduct.route) }
        )
    }
}