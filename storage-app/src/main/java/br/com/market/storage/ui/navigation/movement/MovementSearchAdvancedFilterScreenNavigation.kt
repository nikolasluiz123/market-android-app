package br.com.market.storage.ui.navigation.movement

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.filter.AdvancedFilterArgs
import br.com.market.core.filter.DateAdvancedFilterArgs
import br.com.market.core.filter.NumberAdvancedFilterArgs
import br.com.market.storage.ui.screens.movement.MovementSearchAdvancedFilterScreen
import br.com.market.storage.ui.viewmodels.movements.MovementSearchAdvancedFilterViewModel

internal const val advancedFilterScreen = "movementSearchAdvancedFiltersScreen"

fun NavGraphBuilder.movementSearchAdvancedFiltersScreen(
    onNavigateToTextFilter: (AdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToDateRangeFilter: (DateAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToNumberFilter: (NumberAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit
) {
    composable(
        route = advancedFilterScreen
    ) {
        val viewModel = hiltViewModel<MovementSearchAdvancedFilterViewModel>()

        MovementSearchAdvancedFilterScreen(
            viewModel = viewModel,
            onNavigateToTextFilter = onNavigateToTextFilter,
            onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
            onNavigateToNumberFilter = onNavigateToNumberFilter,
            onNavigateToUserLovFilter = onNavigateToUserLovFilter
        )
    }
}

fun NavController.navigateToMovementSearchAdvancedFilterScreen(navOptions: NavOptions? = null) {
    navigate(route = advancedFilterScreen, navOptions = navOptions)
}