package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.market.storage.ui.screens.FormProductScreen
import br.com.market.storage.ui.viewmodels.FormProductViewModel
import br.com.market.storage.ui.viewmodels.LoginViewModel

fun NavGraphBuilder.formProductGraph(
    navController: NavHostController
) {
    composable(
        route = "${StorageAppDestinations.FormProduct.route}?productId={productId}",
        arguments = listOf(
            navArgument("productId") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        val formProductViewModel = hiltViewModel<FormProductViewModel>()
        val loginViewModel = hiltViewModel<LoginViewModel>()

        FormProductScreen(
            viewModel = formProductViewModel,
            onLogoutClick = {
                loginViewModel.logout()

                navController.inclusiveNavigation(
                    originRoute = StorageAppDestinations.StorageProducts.route,
                    destinyRoute = StorageAppDestinations.Login.route
                )
            },
            onBackClick = {
                navController.popBackStack()
            },
            onAfterDeletePoduct = {
                navController.popBackStack()
            }
        )
    }
}