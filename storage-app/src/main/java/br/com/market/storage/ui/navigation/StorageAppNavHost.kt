package br.com.market.storage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

/**
 * Host de Navegação que configura o grafo do APP
 *
 * @param modifier Modificadores específico.
 * @param navController Controlador da navegação do compose.
 *
 * @author Nikolas Luiz Schmitt
 */
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

        cameraGraph(navController)
        androidGalleryGraph(navController)
        loadImageLinkGraph(navController)

        testesGraph(navController)
    }
}

/**
 * Função para realizar a navegação limpando a backstack.
 *
 * @param route Rota para onde deseja navegar.
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavHostController.cleanNavigation(route: String) {
    this.navigate(route) { popUpTo(0) }
}

/**
 * Função para realizar a navegação limpando da backstack a origem.
 *
 * @param originRoute Rota de Origem (onde o usuário está)
 * @param destinyRoute Rota de Destino (para onde o usuário irá)
 */
fun NavHostController.inclusiveNavigation(originRoute: String, destinyRoute: String) {
    this.navigate(destinyRoute) {
        popUpTo(originRoute) {
            inclusive = true
        }
    }
}