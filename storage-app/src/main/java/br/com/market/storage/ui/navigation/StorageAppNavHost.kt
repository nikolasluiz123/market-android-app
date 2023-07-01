package br.com.market.storage.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import br.com.market.core.ui.components.bottomsheet.IEnumOptionsBottomSheet
import br.com.market.core.ui.components.bottomsheet.loadimage.EnumOptionsBottomSheetLoadImage
import br.com.market.storage.ui.navigation.brand.brandScreen
import br.com.market.storage.ui.navigation.brand.navigateToBrandScreen
import br.com.market.storage.ui.navigation.category.categoryScreen
import br.com.market.storage.ui.navigation.category.categorySearchScreen
import br.com.market.storage.ui.navigation.category.categorySearchScreenRoute
import br.com.market.storage.ui.navigation.category.navigateToCategoryScreen
import br.com.market.storage.ui.navigation.category.navigateToCategorySearchScreen
import br.com.market.storage.ui.navigation.lovs.brandLov
import br.com.market.storage.ui.navigation.lovs.brandLovNavResultCallbackKey
import br.com.market.storage.ui.navigation.lovs.navigateToBrandLov
import br.com.market.storage.ui.navigation.lovs.navigateToProductLov
import br.com.market.storage.ui.navigation.lovs.productLov
import br.com.market.storage.ui.navigation.lovs.productLovNavResultCallbackKey
import br.com.market.storage.ui.navigation.movement.movementScreen
import br.com.market.storage.ui.navigation.movement.movementsSearchScreen
import br.com.market.storage.ui.navigation.movement.navigateToMovementScreen
import br.com.market.storage.ui.navigation.movement.navigateToMovementsSearchScreen
import br.com.market.storage.ui.navigation.product.navigateToProductScreen
import br.com.market.storage.ui.navigation.product.productScreen
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

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
            },
            onNavigateToAbout = {
                navController.navigateToAboutScreen(showBack = false, navOptions {
                    popUpTo(splashScreenRoute) {
                        inclusive = true
                    }
                })
            }
        )

        aboutScreen(
            onNavigateToBack = navController::popBackStack
        )

        loginScreen(
            onNavigateToCategoryScreen = {
                navController.navigateToCategorySearchScreen(navOptions {
                    popUpTo(splashScreenRoute) {
                        inclusive = true
                    }
                })
            },
            onNavigateToRegisterUserScreen = navController::navigateToRegisterUserScreen
        )

        registerUserGraph(
            onNavigateToBack = navController::popBackStack,
            onNavigateToLoginScreen = navController::navigateToLoginScreen
        )

        categorySearchScreen(
            onAddCategoryClick = navController::navigateToCategoryScreen,
            onCategoryClick = navController::navigateToCategoryScreen,
            onAfterLogout = {
                navController.navigateToLoginScreen(navOptions {
                    popUpTo(categorySearchScreenRoute) {
                        inclusive = true
                    }
                })
            },
            onAboutClick = navController::navigateToAboutScreen
        )

        categoryScreen(
            onBackClick = navController::popBackStack,
            onFabAddBrandClick = navController::navigateToBrandScreen,
            onBrandItemClick = navController::navigateToBrandScreen
        )

        brandScreen(
            onBackClick = navController::popBackStack,
            onNavToBrandLov = navController::navigateToBrandLov,
            onFabAddProductClick = navController::navigateToProductScreen,
            onProductClick = navController::navigateToProductScreen,
            onStorageButtonClick = navController::navigateToMovementsSearchScreen
        )

        productScreen(
            onBackClick = navController::popBackStack,
            onStorageButtonClick = navController::navigateToMovementsSearchScreen,
            onBottomSheetLoadImageItemClick = navController::navigateToLoadImage,
            onProductImageClick = navController::navigateToImageViewerScreen
        )

        movementsSearchScreen(
            onBackClick = navController::popBackStack,
            onAddMovementClick = navController::navigateToMovementScreen,
            onMovementClick = {
                navController.navigateToMovementScreen(it.categoryId, it.brandId, it.operationType, it.productId, it.id)
            }
        )

        movementScreen(
            onBackClick = navController::popBackStack,
            onNavToProductLov = navController::navigateToProductLov,
            onInactivate = navController::popBackStack,
            onConfirmInputClick = navController::popBackStack
        )

        brandLov(
            onItemClick = { brandId ->
                navController.popBackStackWithResult(brandLovNavResultCallbackKey, brandId)
            },
            onBackClick = navController::popBackStack
        )

        productLov(
            onItemClick = { productId ->
                navController.popBackStackWithResult(productLovNavResultCallbackKey, productId)
            },
            onBackClick = navController::popBackStack
        )

        cameraGraph(
            onImageCaptured = { uri, _ ->
                // Isso talvez seja temporário, preciso descobrir se tem uma forma de remover
                // essa execução de dentro do contexto IO da courotine
                scope.launch(Main) {
                    navController.popBackStackWithResult(cameraNavResultCallbackKey, uri)
                }
            },
            onError = {

            }
        )

        androidGalleryGraph(
            onAfterShowGallery = navController::popBackStack,
            onImageCaptured = {
                navController.popBackStackWithResult(androidGalleryNavResultCallbackKey, it)
            }
        )

        loadImageLinkGraph(
            onNavigationIconClick = navController::popBackStack,
            onSaveClick = {

            }
        )

        imageViewerScreen(
            onBackClick = navController::popBackStack,
            onAfterDeleteImage = navController::popBackStack,
            onBottomSheetLoadImageItemClick = navController::navigateToLoadImage,
            onAfterSaveProductImage = navController::popBackStack
        )
    }
}

fun NavController.navigateToLoadImage(
    option: IEnumOptionsBottomSheet,
    callback: (Uri) -> Unit
) {
    when (option) {
        EnumOptionsBottomSheetLoadImage.CAMERA -> {
            navigateForResult(
                key = cameraNavResultCallbackKey,
                route = cameraScreenRoute,
                callback = callback
            )
        }
        EnumOptionsBottomSheetLoadImage.GALLERY -> {
            navigateForResult(
                key = androidGalleryNavResultCallbackKey,
                route = androidGalleryScreenRoute,
                callback = callback
            )
        }
        EnumOptionsBottomSheetLoadImage.LINK -> {
            navigateForResult(
                key = loadImageLinkNavResultCallbackKey,
                route = loadImageLinkScreenRoute,
                callback = callback
            )
        }
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