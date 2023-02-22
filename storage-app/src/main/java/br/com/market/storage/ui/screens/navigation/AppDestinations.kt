package br.com.market.storage.ui.screens.navigation

sealed class AppDestinations(val route: String) {
    object Splash : AppDestinations("splash_screen")
    object Login : AppDestinations("login")
    object StorageProducts : AppDestinations("storageProducts")
    object FormProduct : AppDestinations("formProduct")
}
