package br.com.market.storage.ui.navigation

sealed class StorageAppDestinations(val route: String) {
    object Splash : StorageAppDestinations("splash_screen")
    object Login : StorageAppDestinations("login")
    object RegisterUser : StorageAppDestinations("registerUser")
    object StorageProducts : StorageAppDestinations("storageProducts")
    object FormProduct : StorageAppDestinations("formProduct")
}
