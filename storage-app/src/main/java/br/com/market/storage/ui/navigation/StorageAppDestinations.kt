package br.com.market.storage.ui.navigation

/**
 * Classe que contém os destinos da aplicação, permite a
 * centralização das rotas e evita erros de escrita.
 *
 * @property route Rota de redirecionamento, cada compose recebe sua rota no grafo.
 *
 * @author Nikolas Luiz Schmitt
 */
sealed class StorageAppDestinations(val route: String) {
    object Splash : StorageAppDestinations("splash_screen")
    object Login : StorageAppDestinations("login")
    object RegisterUser : StorageAppDestinations("registerUser")
    object StorageProducts : StorageAppDestinations("storageProducts")
    object FormProduct : StorageAppDestinations("formProduct")
    object Camera : StorageAppDestinations("camera")
    object AndroidGallery : StorageAppDestinations("androidGallery")
    object LoadImageLink : StorageAppDestinations("loadImageLink")
    object Testes : StorageAppDestinations("testes")
}
