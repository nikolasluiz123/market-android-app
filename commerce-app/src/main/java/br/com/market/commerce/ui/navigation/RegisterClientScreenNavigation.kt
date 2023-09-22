package br.com.market.commerce.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.commerce.ui.screens.RegisterClientScreen
import br.com.market.commerce.ui.viewmodel.RegisterClientViewModel
import br.com.market.core.filter.arguments.InputArgs
import br.com.market.core.filter.arguments.InputNumberArgs
import br.com.market.core.filter.arguments.InputPasswordArgs

internal const val registerClientScreenRoute = "registerClientScreenRoute"

fun NavGraphBuilder.registerClientScreen(
    onBackClick: () -> Unit,
    onNavigateToTextInput: (args: InputArgs, callback: (String?) -> Unit) -> Unit,
    onNavigateToNumberInput: (args: InputNumberArgs, callback: (Number?) -> Unit) -> Unit,
    onNavigateToPasswordInput: (args: InputPasswordArgs, callback: (String?) -> Unit) -> Unit

) {
    composable(route = registerClientScreenRoute) {
        val registerClientViewModel = hiltViewModel<RegisterClientViewModel>()

        RegisterClientScreen(
            viewModel = registerClientViewModel,
            onBackClick = onBackClick,
            onNavigateToTextInput = onNavigateToTextInput,
            onNavigateToNumberInput = onNavigateToNumberInput,
            onNavigateToPasswordInput = onNavigateToPasswordInput
        )
    }
}

fun NavController.navigateToRegisterClientScreen(navOptions: NavOptions? = null) {
    navigate(route = registerClientScreenRoute, navOptions = navOptions)
}