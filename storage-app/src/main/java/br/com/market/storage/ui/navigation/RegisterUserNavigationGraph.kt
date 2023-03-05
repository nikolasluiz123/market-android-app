package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.RegisterUserScreen
import br.com.market.storage.ui.viewmodels.RegisterUserViewModel

fun NavGraphBuilder.registerUserGraph(
    navController: NavHostController
) {
    composable(route = StorageAppDestinations.RegisterUser.route) {
        val registerUserViewModel = hiltViewModel<RegisterUserViewModel>()
        RegisterUserScreen(
            viewModel = registerUserViewModel,
            onNavigationClick = { navController.popBackStack() },
            onRegisterSuccess = {
                navController.inclusiveNavigation(
                    originRoute = StorageAppDestinations.RegisterUser.route,
                    destinyRoute = StorageAppDestinations.Login.route
                )
            }
        )
    }
}