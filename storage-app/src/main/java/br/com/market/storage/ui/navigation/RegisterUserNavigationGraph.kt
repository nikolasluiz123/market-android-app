package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.RegisterUserScreen
import br.com.market.storage.ui.viewmodels.RegisterUserViewModel

/**
 * Função que realiza as definições da navegação para a
 * tela de Cadastro do Usuário.
 *
 * @param navController Controlador da navegação do compose.
 *
 * @author Nikolas Luiz Schmitt
 */
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