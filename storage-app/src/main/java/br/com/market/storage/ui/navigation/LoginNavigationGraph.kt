package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.LoginScreen
import br.com.market.storage.ui.viewmodels.LoginViewModel

fun NavGraphBuilder.loginGraph(
    navController: NavHostController
) {
    composable(route = StorrageAppDestinations.Login.route) {
        val loginViewModel = hiltViewModel<LoginViewModel>()

        LoginScreen(
            viewModel = loginViewModel,
            onLoginClick = {
                navController.inclusiveNavigation(
                    originRoute = StorrageAppDestinations.Login.route,
                    destinyRoute = StorrageAppDestinations.StorageProducts.route
                )
            }
        )
    }
}