package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.market.storage.ui.screens.FormProductScreen
import br.com.market.storage.ui.viewmodels.FormProductViewModel

fun NavGraphBuilder.formProductGraph(
    navController: NavHostController
) {
    composable(
        route = "${StorrageAppDestinations.FormProduct.route}?productId={productId}",
        arguments = listOf(
            navArgument("productId") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        val formProductViewModel = hiltViewModel<FormProductViewModel>()

        FormProductScreen(
            viewModel = formProductViewModel,
            onLogoutClick = {
                navController.inclusiveNavigation(
                    originRoute = StorrageAppDestinations.StorageProducts.route,
                    destinyRoute = StorrageAppDestinations.Login.route
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