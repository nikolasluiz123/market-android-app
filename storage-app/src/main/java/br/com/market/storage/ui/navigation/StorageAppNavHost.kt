package br.com.market.storage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import br.com.market.storage.ui.navigation.brand.brandScreen
import br.com.market.storage.ui.navigation.brand.navigateToBrandScreen
import br.com.market.storage.ui.navigation.category.categoryScreen
import br.com.market.storage.ui.navigation.category.categorySearchScreen
import br.com.market.storage.ui.navigation.category.navigateToCategoryScreen
import br.com.market.storage.ui.navigation.category.navigateToCategorySearchScreen
import br.com.market.storage.ui.navigation.lovs.brandLov
import br.com.market.storage.ui.navigation.lovs.navigateToBrandLov

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
                navController.navigateToCategorySearchScreen(navOptions {
                    popUpTo(splashScreenRoute) {
                        inclusive = true
                    }
                })
            }
        )

        loginScreen(
            onNavigateToCategoryScreen = {
                navController.navigateToCategorySearchScreen(navOptions {
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

        categorySearchScreen(
            onAddCategoryClick = { navController.navigateToCategoryScreen() },
            onCategoryClick = {
                navController.navigateToCategoryScreen(categoryId = it)
            }
        )

        categoryScreen(
            onBackClick = { navController.popBackStack() },
            onFabAddBrandClick = { categoryId ->
                navController.navigateToBrandScreen(categoryId = categoryId)
            },
            onBrandItemClick = { categoryId, brandId ->
                navController.navigateToBrandScreen(categoryId = categoryId, brandId = brandId)
            }
        )

        brandScreen(
            onBackClick = { navController.popBackStack() },
            onNavToBrandLov = { categoryId ->
                navController.navigateToBrandLov(categoryId)
            }
        )

        brandLov(
            onBackClick = { navController.popBackStack() },
            onItemClick = { navController.popBackStack() }
        )

        cameraGraph()

        androidGalleryGraph(onAfterShowGallery = { navController.popBackStack() })

        loadImageLinkGraph(navController)
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