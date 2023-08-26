package br.com.market.core.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.filter.DateAdvancedFilterArgs
import br.com.market.core.gson.LocalDateTimeAdapter
import br.com.market.core.ui.components.filter.DateRangeAdvancedFilter
import br.com.market.core.ui.viewmodel.filter.DateRangeAdvancedFilterViewModel
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

internal const val dateRangeAdvancedFilterScreenRoute = "dateRangeAdvancedFilterScreenRoute"
const val dateRangeAdvancedFilterScreenNavResultCallbackKey = "dateRangeAdvancedFilterScreenNavResultCallbackKey"
internal const val dateRangeAdvancedFilterArguments = "dateRangeAdvancedFilterArguments"

fun NavGraphBuilder.dateRangeAdvancedFilterScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (Pair<LocalDateTime?, LocalDateTime?>?) -> Unit
) {
    composable(
        route = "$dateRangeAdvancedFilterScreenRoute?$dateRangeAdvancedFilterArguments={$dateRangeAdvancedFilterArguments}"
    ) {
        val viewModel = hiltViewModel<DateRangeAdvancedFilterViewModel>()

        DateRangeAdvancedFilter(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToDateRangeAdvancedFilterScreen(
    args: DateAdvancedFilterArgs,
    callback: (Pair<LocalDateTime, LocalDateTime>) -> Unit,
) {
    val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter()).create()
    val json = gson.getAdapter(DateAdvancedFilterArgs::class.java).toJson(args)

    navigateForResult(
        key = dateRangeAdvancedFilterScreenNavResultCallbackKey,
        route = "$dateRangeAdvancedFilterScreenRoute?$dateRangeAdvancedFilterArguments={$json}",
        callback = callback
    )
}