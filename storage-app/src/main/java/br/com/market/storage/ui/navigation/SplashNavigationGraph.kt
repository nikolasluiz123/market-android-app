package br.com.market.storage.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.SplashScreen

fun NavGraphBuilder.splashGraph(
    navController: NavHostController
) {
    composable(route = StorrageAppDestinations.Splash.route) {
        SplashScreen(
            onAfterDelay = {
                navController.cleanNavigation(StorrageAppDestinations.Login.route)
            }
        )
    }
}