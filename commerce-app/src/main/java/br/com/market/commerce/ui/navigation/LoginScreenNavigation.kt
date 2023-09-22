package br.com.market.commerce.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.commerce.ui.screens.LoginScreen
import br.com.market.commerce.ui.viewmodel.LoginViewModel

internal const val loginScreenRoute = "loginScreenRoute"

fun NavGraphBuilder.loginScreen(
    onRegisterClick: () -> Unit,
    onAuthenticateSuccess: () -> Unit
) {
    composable(route = loginScreenRoute) {
        val loginViewModel = hiltViewModel<LoginViewModel>()

        LoginScreen(
            viewModel = loginViewModel,
            onRegisterClick = onRegisterClick,
            onAuthenticateSuccess = onAuthenticateSuccess
        )
    }
}

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(route = loginScreenRoute, navOptions = navOptions)
}