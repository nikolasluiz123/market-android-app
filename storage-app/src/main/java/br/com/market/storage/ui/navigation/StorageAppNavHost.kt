package br.com.market.storage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import br.com.market.core.ui.navigation.dateRangeAdvancedFilterScreen
import br.com.market.core.ui.navigation.dateRangeAdvancedFilterScreenNavResultCallbackKey
import br.com.market.core.ui.navigation.navigateToDateRangeAdvancedFilterScreen
import br.com.market.core.ui.navigation.navigateToNumberAdvancedFilterScreen
import br.com.market.core.ui.navigation.navigateToTextAdvancedFilterScreen
import br.com.market.core.ui.navigation.numberAdvancedFilterScreen
import br.com.market.core.ui.navigation.numberAdvancedFilterScreenNavResultCallbackKey
import br.com.market.core.ui.navigation.popBackStackWithResult
import br.com.market.core.ui.navigation.textAdvancedFilterScreen
import br.com.market.core.ui.navigation.textAdvancedFilterScreenNavResultCallbackKey
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
import br.com.market.storage.ui.navigation.lovs.navigateToUserLov
import br.com.market.storage.ui.navigation.lovs.productLov
import br.com.market.storage.ui.navigation.lovs.productLovNavResultCallbackKey
import br.com.market.storage.ui.navigation.lovs.userLov
import br.com.market.storage.ui.navigation.lovs.userLovNavResultCallbackKey
import br.com.market.storage.ui.navigation.movement.movementScreen
import br.com.market.storage.ui.navigation.movement.movementSearchAdvancedFilterNavResultCallbackKey
import br.com.market.storage.ui.navigation.movement.movementSearchAdvancedFiltersScreen
import br.com.market.storage.ui.navigation.movement.movementsSearchScreen
import br.com.market.storage.ui.navigation.movement.navigateToMovementScreen
import br.com.market.storage.ui.navigation.movement.navigateToMovementSearchAdvancedFilterScreen
import br.com.market.storage.ui.navigation.movement.navigateToMovementsSearchScreen
import br.com.market.storage.ui.navigation.product.navigateToProductScreen
import br.com.market.storage.ui.navigation.product.productScreen

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
            onAboutClick = navController::navigateToAboutScreen
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
            onProductImageClick = navController::navigateToImageViewerScreen,
            onBottomSheetLoadImageLinkClick = navController::navigateToLoadImageLinkScreen
        )

        movementsSearchScreen(
            onBackClick = navController::popBackStack,
            onAddMovementClick = navController::navigateToMovementScreen,
            onMovementClick = {
                navController.navigateToMovementScreen(it.categoryId, it.brandId, it.operationType, it.productId, it.id)
            },
            onAdvancedFiltersClick = navController::navigateToMovementSearchAdvancedFilterScreen
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

        userLov(
            onItemClick = { userPair ->
                navController.popBackStackWithResult(userLovNavResultCallbackKey, userPair)
            },
            onBackClick = navController::popBackStack
        )

        loadImageLinkGraph(
            onNavigationIconClick = navController::popBackStack,
            onSaveClick = {
                navController.popBackStackWithResult(loadImageLinkNavResultCallbackKey, it)
            }
        )

        imageViewerScreen(
            onBackClick = navController::popBackStack,
            onAfterDeleteImage = navController::popBackStack,
            onAfterSaveProductImage = navController::popBackStack
        )

        movementSearchAdvancedFiltersScreen(
            onNavigateToTextFilter = { args, callback ->
                navController.navigateToTextAdvancedFilterScreen(args, callback)
            },
            onNavigateToDateRangeFilter = { args, callback ->
                navController.navigateToDateRangeAdvancedFilterScreen(args, callback)
            },
            onNavigateToNumberFilter = { args, callback ->
                navController.navigateToNumberAdvancedFilterScreen(args, callback)
            },
            onNavigateToUserLovFilter = { callback ->
                navController.navigateToUserLov(callback)
            },
            onApplyFilters = {
                navController.popBackStackWithResult(movementSearchAdvancedFilterNavResultCallbackKey, it)
            }
        )

        textAdvancedFilterScreen(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(textAdvancedFilterScreenNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )

        dateRangeAdvancedFilterScreen(
            onBackClick = navController::popBackStack,
            onConfirmClick = { pair ->
                navController.popBackStackWithResult(dateRangeAdvancedFilterScreenNavResultCallbackKey, pair)
            },
            onCancelClick = navController::popBackStack
        )

        numberAdvancedFilterScreen(
            onBackClick = navController::popBackStack,
            onConfirmClick = {
                navController.popBackStackWithResult(numberAdvancedFilterScreenNavResultCallbackKey, it)
            },
            onCancelClick = navController::popBackStack
        )
    }
}