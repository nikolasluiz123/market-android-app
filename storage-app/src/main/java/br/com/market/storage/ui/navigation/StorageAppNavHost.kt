package br.com.market.storage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions

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
        startDestination = splashScreenRoute,
        modifier = modifier
    ) {

        splashScreen(
            onNavigateToLogin = {
                navController.navigateToLoginScreen(navOptions {
                    popUpTo(loginScreenRoute) {
                        inclusive = true
                    }
                })
            },
            onNavigateToCategories = {
                navController.navigateToCategoryScreen(navOptions {
                    popUpTo(splashScreenRoute) {
                        inclusive = true
                    }
                })
            }
        )

        loginScreen(
            onNavigateToCategoryScreen = {
                navController.navigateToCategoryScreen(navOptions {
                    popUpTo(splashScreenRoute) {
                        inclusive = true
                    }
                })
            },
            onNavigateToRegisterUserScreen = { navController.navigateToRegisterUserScreen() }
        )

        registerUserGraph(
            onNavigateToBack = { navController.popBackStack() },
            onNavigateToLoginScreen = { navController.navigateToLoginScreen() }
        )

        categoryScreen(
            onButtonBackClickFailureScreen = navController::popBackStack
        )

        storageProductsGraph(navController)
        formProductGraph(navController)

        cameraGraph(navController)
        androidGalleryGraph(navController)
        loadImageLinkGraph(navController)

        testesGraph(navController)
    }
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