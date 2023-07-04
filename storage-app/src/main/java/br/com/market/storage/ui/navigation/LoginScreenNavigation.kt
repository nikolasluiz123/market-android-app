package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.LoginScreen
import br.com.market.storage.ui.viewmodels.LoginViewModel

internal const val loginScreenRoute = "login"

/**
 * Função que realiza as definições da navegação para a
 * tela de Login.
 *
 * @param onNavigateToRegisterUserScreen Função para navegar até a tela de cadastro de usuário.
 * @param onNavigateToCategoryScreen Função para navegar até a tela de categorias.
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavGraphBuilder.loginScreen(
    onNavigateToCategoryScreen: () -> Unit
) {
    composable(route = loginScreenRoute) {
        val loginViewModel = hiltViewModel<LoginViewModel>()

        LoginScreen(
            viewModel = loginViewModel,
            onAuthenticateSuccess = {
                onNavigateToCategoryScreen()
            }
        )
    }
}

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(route = loginScreenRoute, navOptions = navOptions)
}