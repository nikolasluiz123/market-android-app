package br.com.market.storage.ui.navigation.movement

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.filter.AdvancedFilterArgs
import br.com.market.core.filter.DateAdvancedFilterArgs
import br.com.market.core.filter.NumberAdvancedFilterArgs
import br.com.market.core.gson.LocalDateTimeAdapter
import br.com.market.core.ui.navigation.navigateForResult
import br.com.market.localdataaccess.filter.MovementSearchScreenFilters
import br.com.market.storage.ui.screens.movement.MovementSearchAdvancedFilterScreen
import br.com.market.storage.ui.viewmodels.movements.MovementSearchAdvancedFilterViewModel
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

internal const val movementSerarchAdvancedFilterScreen = "movementSearchAdvancedFiltersScreen"
internal const val movementSearchAdvancedFilterNavResultCallbackKey = "movementSearchAdvancedFilterNavResultCallbackKey"
internal const val argumentMovementSearchAdvancedFilterJson = "argumentMovementSearchAdvancedFilterJson"

fun NavGraphBuilder.movementSearchAdvancedFiltersScreen(
    onNavigateToTextFilter: (AdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToDateRangeFilter: (DateAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToNumberFilter: (NumberAdvancedFilterArgs, (Any) -> Unit) -> Unit,
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit,
    onApplyFilters: (MovementSearchScreenFilters) -> Unit
) {
    composable(
        route = "$movementSerarchAdvancedFilterScreen?$argumentMovementSearchAdvancedFilterJson={$argumentMovementSearchAdvancedFilterJson}"
    ) {
        val viewModel = hiltViewModel<MovementSearchAdvancedFilterViewModel>()

        MovementSearchAdvancedFilterScreen(
            viewModel = viewModel,
            onNavigateToTextFilter = onNavigateToTextFilter,
            onNavigateToDateRangeFilter = onNavigateToDateRangeFilter,
            onNavigateToNumberFilter = onNavigateToNumberFilter,
            onNavigateToUserLovFilter = onNavigateToUserLovFilter,
            onApplyFilters = onApplyFilters
        )
    }
}

fun NavController.navigateToMovementSearchAdvancedFilterScreen(filter: MovementSearchScreenFilters, callback: (MovementSearchScreenFilters) -> Unit) {
    val json = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
        .getAdapter(MovementSearchScreenFilters::class.java)
        .toJson(filter)

    navigateForResult(
        key = movementSearchAdvancedFilterNavResultCallbackKey,
        route = "$movementSerarchAdvancedFilterScreen?$argumentMovementSearchAdvancedFilterJson={$json}",
        callback = callback
    )
}