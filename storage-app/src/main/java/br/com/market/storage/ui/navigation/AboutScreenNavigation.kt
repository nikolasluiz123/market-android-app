package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.AboutScreen
import br.com.market.storage.ui.viewmodels.AboutViewModel

internal const val aboutScreenRoute = "about"
internal const val argumentShowBack = "showBack"

fun NavGraphBuilder.aboutScreen(
    onNavigateToBack: () -> Unit,
) {
    composable(route = "$aboutScreenRoute?$argumentShowBack={$argumentShowBack}") {
        val aboutViewModel = hiltViewModel<AboutViewModel>()

        AboutScreen(
            viewModel = aboutViewModel,
            onBackClick = onNavigateToBack
        )
    }
}

fun NavController.navigateToAboutScreen(showBack: Boolean = true, navOptions: NavOptions? = null) {
    navigate(route = "$aboutScreenRoute?$argumentShowBack={$showBack}", navOptions = navOptions)
}