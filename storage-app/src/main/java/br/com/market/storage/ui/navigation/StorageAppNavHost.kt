package br.com.market.storage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import br.com.market.storage.ui.navigation.brand.brandScreen
import br.com.market.storage.ui.navigation.brand.navigateToBrandScreen
import br.com.market.storage.ui.navigation.category.*
import br.com.market.storage.ui.navigation.lovs.brandLov
import br.com.market.storage.ui.navigation.lovs.brandLovNavResultCallbackKey
import br.com.market.storage.ui.navigation.lovs.brandLovRoute

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
            onNavToBrandLov = { categoryId, callback ->
                navController.navigateForResult(
                    key = brandLovNavResultCallbackKey,
                    route = "$brandLovRoute?$argumentCategoryId={$categoryId}",
                    callback = callback
                )
            }
        )

        brandLov(
            onBackClick = { navController.popBackStack() },
            onItemClick = { brandId ->
                navController.popBackStackWithResult(brandLovNavResultCallbackKey, brandId)
            }
        )

        cameraGraph()

        androidGalleryGraph(onAfterShowGallery = { navController.popBackStack() })

        loadImageLinkGraph(navController)
    }
}

/**
 * Função para definir um valor no state handle da backstack de navegação
 * e recuperar futuramente em algum ponto desejado.
 *
 * @param T Tipo do valor passado para o callback
 * @param key Chave que aponta para o valor setado em [androidx.lifecycle.SavedStateHandle]
 * @param callback Função que pode executar algo usando o valor retornado
 *
 * @author Nikolas Luiz Schmitt
 */
fun <T> NavController.setNavResultCallback(key: String, callback: (T) -> Unit) {
    currentBackStackEntry?.savedStateHandle?.set(key, callback)
}

/**
 * Função que retorna o callback que foi definido para que possa
 * ser executado.
 *
 * @param T Tipo do valor passado para o callback
 * @param key Chave que aponta para o valor setado em [androidx.lifecycle.SavedStateHandle]
 *
 * @author Nikolas Luiz Schmitt
 */
fun <T> NavController.getNavResultCallback(key: String): ((T) -> Unit)? {
    return previousBackStackEntry?.savedStateHandle?.remove(key)
}

/**
 * Função que recupera o callback utilizando [getNavResultCallback]
 * e executa passando como resultado algum valor.
 *
 * Após invocar o callback é feito o [NavController.popBackStack] para
 * remover da pilha a tela.
 *
 * @param T Tipo do valor passado para o callback
 * @param key Chave que aponta para o valor setado em [androidx.lifecycle.SavedStateHandle]
 * @param result O que desejar enviar para a tela anterior
 *
 * @author Nikolas Luiz Schmitt
 */
fun <T> NavController.popBackStackWithResult(key: String, result: T) {
    getNavResultCallback<T>(key)?.invoke(result)
    popBackStack()
}

/**
 * Função para navegar para um tela definindo um callback
 * usando [setNavResultCallback] para ser executado em algum
 * momento usando [popBackStackWithResult]
 *
 * @param T Tipo do valor passado para o callback
 * @param key Chave que aponta para o valor setado em [androidx.lifecycle.SavedStateHandle]
 * @param route Rota para onde deve ocorrer a navegação
 * @param callback Função que pode executar algo usando o valor retornado
 * @param navOptions Atributo que pode ser usado para definições da navegação
 * @param navigatorExtras Possibilita algum comportamento específico
 *
 * @author Nikolas Luiz Schmitt
 */
fun <T> NavController.navigateForResult(
    key: String,
    route: String,
    callback: (T) -> Unit,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    setNavResultCallback(key, callback)
    navigate(route, navOptions, navigatorExtras)
}