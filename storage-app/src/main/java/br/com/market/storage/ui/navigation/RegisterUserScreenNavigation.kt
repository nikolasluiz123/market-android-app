package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.RegisterUserScreen
import br.com.market.storage.ui.viewmodels.RegisterUserViewModel

internal const val registerUserScreenRoute = "registerUser"

/**
 * Função que realiza as definições da navegação para a
 * tela de Cadastro do Usuário.
 *
 * @param onNavigateToBack Função para retornar a tela anterior
 * @param onNavigateToLoginScreen Função para redirecionar para a tela de login
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavGraphBuilder.registerUserGraph(
    onNavigateToBack: () -> Unit,
    onNavigateToLoginScreen: () -> Unit
) {
    composable(route = registerUserScreenRoute) {
        val registerUserViewModel = hiltViewModel<RegisterUserViewModel>()

        RegisterUserScreen(
            viewModel = registerUserViewModel,
            onBackClick = onNavigateToBack,
            onNavigateToLoginScreen = onNavigateToLoginScreen
        )
    }
}

fun NavController.navigateToRegisterUserScreen(navOptions: NavOptions? = null) {
    navigate(route = registerUserScreenRoute, navOptions = navOptions)
}