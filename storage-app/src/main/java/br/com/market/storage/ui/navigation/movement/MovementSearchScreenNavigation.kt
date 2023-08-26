package br.com.market.storage.ui.navigation.movement

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.enums.EnumOperationType
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.localdataaccess.tuples.StorageOperationHistoryTuple
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.screens.movement.MovementsSearchScreen
import br.com.market.storage.ui.viewmodels.movements.MovementsSearchViewModel

internal const val movementsSearchScreenRoute = "movementsSearch"

fun NavGraphBuilder.movementsSearchScreen(
    onBackClick: () -> Unit,
    onAddMovementClick: (String, String, EnumOperationType, String?) -> Unit,
    onMovementClick: (StorageOperationHistoryTuple) -> Unit,
    onAdvancedFiltersClick: (MovementSearchScreenFilters, (MovementSearchScreenFilters) -> Unit) -> Unit,
) {
    composable(route = "$movementsSearchScreenRoute?$argumentCategoryId={$argumentCategoryId}&$argumentBrandId={$argumentBrandId}&$argumentProductId={$argumentProductId}") {
        val viewModel = hiltViewModel<MovementsSearchViewModel>()

        MovementsSearchScreen(
            viewModel = viewModel,
            onAddMovementClick = onAddMovementClick,
            onBackClick = onBackClick,
            onMovementClick = onMovementClick,
            onAdvancedFiltersClick = onAdvancedFiltersClick,
        )
    }
}

fun NavController.navigateToMovementsSearchScreen(
    categoryId: String,
    brandId: String,
    productId: String? = null,
    navOptions: NavOptions? = null
) {
    navigate(
        route = "$movementsSearchScreenRoute?$argumentCategoryId={$categoryId}&$argumentBrandId={$brandId}&$argumentProductId={$productId}",
        navOptions = navOptions
    )
}