package br.com.market.core.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.filter.AdvancedFilterArgs
import br.com.market.core.ui.components.filter.DateRangeAdvancedFilterScreen
import br.com.market.core.ui.viewmodel.filter.DateRangeAdvancedFilterViewModel
import com.google.gson.Gson
import java.time.LocalDateTime

internal const val dateRangeAdvancedFilterScreenRoute = "dateRangeAdvancedFilterScreenRoute"
const val dateRangeAdvancedFilterScreenNavResultCallbackKey = "dateRangeAdvancedFilterScreenNavResultCallbackKey"
internal const val dateRangeAdvancedFilterArguments = "dateRangeAdvancedFilterArguments"

fun NavGraphBuilder.dateRangeAdvancedFilterScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (LocalDateTime, LocalDateTime) -> Unit
) {
    composable(
        route = "$dateRangeAdvancedFilterScreenRoute?$dateRangeAdvancedFilterArguments={$dateRangeAdvancedFilterArguments}"
    ) {
        val viewModel = hiltViewModel<DateRangeAdvancedFilterViewModel>()

        DateRangeAdvancedFilterScreen(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToDateRangeAdvancedFilterScreen(
    args: AdvancedFilterArgs,
    callback: (Pair<LocalDateTime, LocalDateTime>) -> Unit,
) {
    val json = Gson().getAdapter(AdvancedFilterArgs::class.java).toJson(args)

    navigateForResult(
        key = dateRangeAdvancedFilterScreenNavResultCallbackKey,
        route = "$dateRangeAdvancedFilterScreenRoute?$dateRangeAdvancedFilterArguments={$json}",
        callback = callback
    )
}