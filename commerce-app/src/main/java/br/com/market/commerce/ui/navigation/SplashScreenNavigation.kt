package br.com.market.commerce.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.commerce.ui.screens.SplashScreen


internal const val splashScreenRoute = "splashScreenRoute"

fun NavGraphBuilder.splashScreen(
    onNavigateToScreen: () -> Unit
) {
    composable(route = splashScreenRoute) {
        SplashScreen(
            onAfterDelay = onNavigateToScreen
        )
    }
}