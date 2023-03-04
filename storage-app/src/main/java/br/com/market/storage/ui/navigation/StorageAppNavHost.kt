package br.com.market.storage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun StorageAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = StorageAppDestinations.Splash.route,
        modifier = modifier
    ) {
        splashGraph(navController)
        loginGraph(navController)
        registerUserGraph(navController)
        storageProductsGraph(navController)
        formProductGraph(navController)
    }
}

fun NavHostController.cleanNavigation(route: String) {
    this.navigate(route) { popUpTo(0) }
}

fun NavHostController.inclusiveNavigation(originRoute: String, destinyRoute: String) {
    this.navigate(destinyRoute) {
        popUpTo(originRoute) {
            inclusive = true
        }
    }
}