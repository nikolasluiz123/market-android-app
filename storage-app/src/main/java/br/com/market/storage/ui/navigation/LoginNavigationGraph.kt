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
    composable(route = StorageAppDestinations.Login.route) {
        val loginViewModel = hiltViewModel<LoginViewModel>()

        LoginScreen(
            viewModel = loginViewModel,
            onRegisterUserClick = {
                navController.navigate(StorageAppDestinations.RegisterUser.route)
            },
            onAuthenticateSuccess = {
                navController.inclusiveNavigation(
                    originRoute = StorageAppDestinations.Login.route,
                    destinyRoute = StorageAppDestinations.StorageProducts.route
                )
            }
        )
    }
}