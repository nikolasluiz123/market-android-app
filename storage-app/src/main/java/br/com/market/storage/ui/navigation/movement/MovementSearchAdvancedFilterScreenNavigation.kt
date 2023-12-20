package br.com.market.storage.ui.navigation.movement

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.core.inputs.arguments.DateTimeRangeInputArgs
import br.com.market.core.inputs.arguments.InputArgs
import br.com.market.core.inputs.arguments.InputNumberArgs
import br.com.market.core.gson.adapter.LocalDateTimeAdapter
import br.com.market.core.filter.MovementFilters
import br.com.market.storage.ui.screens.movement.MovementSearchAdvancedFilterScreen
import br.com.market.storage.ui.viewmodels.movements.MovementSearchAdvancedFilterViewModel
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

internal const val movementSerarchAdvancedFilterScreen = "movementSearchAdvancedFiltersScreen"
internal const val movementSearchAdvancedFilterNavResultCallbackKey = "movementSearchAdvancedFilterNavResultCallbackKey"
internal const val argumentMovementSearchAdvancedFilterJson = "argumentMovementSearchAdvancedFilterJson"

fun NavGraphBuilder.movementSearchAdvancedFiltersScreen(
    onNavigateToTextFilter: (InputArgs, (Any) -> Unit) -> Unit,
    onNavigateToDateRangeFilter: (DateTimeRangeInputArgs, (Any) -> Unit) -> Unit,
    onNavigateToNumberFilter: (InputNumberArgs, (Number?) -> Unit) -> Unit,
    onNavigateToUserLovFilter: ((Any) -> Unit) -> Unit,
    onApplyFilters: (MovementFilters) -> Unit
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

fun NavController.navigateToMovementSearchAdvancedFilterScreen(filter: MovementFilters, callback: (MovementFilters) -> Unit) {
    val json = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
        .getAdapter(MovementFilters::class.java)
        .toJson(filter)

    navigateForResult(
        key = movementSearchAdvancedFilterNavResultCallbackKey,
        route = "$movementSerarchAdvancedFilterScreen?$argumentMovementSearchAdvancedFilterJson={$json}",
        callback = callback
    )
}