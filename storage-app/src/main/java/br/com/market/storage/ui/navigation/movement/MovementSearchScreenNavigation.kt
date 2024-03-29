package br.com.market.storage.ui.navigation.movement

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.enums.EnumOperationType
import br.com.market.core.filter.MovementFilters
import br.com.market.domain.StorageOperationHistoryReadDomain
import br.com.market.storage.ui.navigation.brand.argumentBrandId
import br.com.market.storage.ui.navigation.category.argumentCategoryId
import br.com.market.storage.ui.navigation.product.argumentProductId
import br.com.market.storage.ui.screens.movement.MovementsSearchScreen
import br.com.market.storage.ui.viewmodels.movements.MovementsSearchViewModel

internal const val movementsSearchScreenRoute = "movementsSearch"

fun NavGraphBuilder.movementsSearchScreen(
    onBackClick: () -> Unit,
    onAddMovementClick: (String, String, EnumOperationType, String?) -> Unit,
    onMovementClick: (StorageOperationHistoryReadDomain) -> Unit,
    onAdvancedFiltersClick: (MovementFilters, (MovementFilters) -> Unit) -> Unit,
    onNavigateToReportList: (directory: String) -> Unit
) {
    composable(route = "$movementsSearchScreenRoute?$argumentCategoryId={$argumentCategoryId}&$argumentBrandId={$argumentBrandId}&$argumentProductId={$argumentProductId}") {
        val viewModel = hiltViewModel<MovementsSearchViewModel>()

        MovementsSearchScreen(
            viewModel = viewModel,
            onAddMovementClick = onAddMovementClick,
            onBackClick = onBackClick,
            onMovementClick = onMovementClick,
            onAdvancedFiltersClick = onAdvancedFiltersClick,
            onNavigateToReportList = onNavigateToReportList
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