package br.com.market.market.compose.components.input.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.core.gson.adapter.LocalDateTimeAdapter
import br.com.market.core.inputs.arguments.DateTimeRangeInputArgs
import br.com.market.market.compose.components.input.viewmodel.InputDateTimeRangeViewModel
import br.com.market.market.compose.components.input.InputDateTimeRange
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

internal const val dateRangeAdvancedFilterScreenRoute = "dateRangeAdvancedFilterScreenRoute"
const val dateTimeRangeNavResultCallbackKey = "dateRangeAdvancedFilterScreenNavResultCallbackKey"
internal const val dateRangeAdvancedFilterArguments = "dateRangeAdvancedFilterArguments"

fun NavGraphBuilder.inputDateTimeRange(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (Pair<LocalDateTime?, LocalDateTime?>?) -> Unit
) {
    composable(
        route = "$dateRangeAdvancedFilterScreenRoute?$dateRangeAdvancedFilterArguments={$dateRangeAdvancedFilterArguments}"
    ) {
        val viewModel = hiltViewModel<InputDateTimeRangeViewModel>()

        InputDateTimeRange(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToInputDateTimeRange(
    args: DateTimeRangeInputArgs,
    callback: (Pair<LocalDateTime, LocalDateTime>) -> Unit,
) {
    val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter()).create()
    val json = gson.getAdapter(DateTimeRangeInputArgs::class.java).toJson(args)

    navigateForResult(
        key = dateTimeRangeNavResultCallbackKey,
        route = "$dateRangeAdvancedFilterScreenRoute?$dateRangeAdvancedFilterArguments={$json}",
        callback = callback
    )
}