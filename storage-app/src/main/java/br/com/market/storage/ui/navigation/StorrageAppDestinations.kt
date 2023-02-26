package br.com.market.storage.ui.navigation

sealed class StorrageAppDestinations(val route: String) {
    object Splash : StorrageAppDestinations("splash_screen")
    object Login : StorrageAppDestinations("login")
    object StorageProducts : StorrageAppDestinations("storageProducts")
    object FormProduct : StorrageAppDestinations("formProduct")
}
