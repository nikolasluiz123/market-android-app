package br.com.market.storage.ui.navigation.movement

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.enums.EnumOperationType
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.screens.movement.MovementScreen
import br.com.market.storage.ui.viewmodels.movements.MovementViewModel

internal const val movementScreenRoute = "movement"
internal const val argumentMovementId = "movementId"
internal const val argumentOperationType = "operationType"

fun NavGraphBuilder.movementScreen(
    onBackClick: () -> Unit,
    onNavToProductLov: (String, String, (String) -> Unit) -> Unit
) {
    composable(route = "$movementScreenRoute?$argumentCategoryId={$argumentCategoryId}&$argumentBrandId={$argumentBrandId}&$argumentProductId={$argumentProductId}&$argumentOperationType={$argumentOperationType}&$argumentMovementId={$argumentMovementId}") {
        val viewModel = hiltViewModel<MovementViewModel>()

        MovementScreen(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onNavToProductLov = onNavToProductLov
        )
    }
}

fun NavController.navigateToMovementScreen(
    categoryId: String,
    brandId: String,
    operationType: EnumOperationType,
    productId: String? = null,
    movementId: String? = null,
    navOptions: NavOptions? = null
) {
    navigate(
        route = "$movementScreenRoute?$argumentCategoryId={$categoryId}&$argumentBrandId={$brandId}&$argumentProductId={$productId}&$argumentOperationType={${operationType.name}&$argumentMovementId={$movementId}",
        navOptions = navOptions
    )
}