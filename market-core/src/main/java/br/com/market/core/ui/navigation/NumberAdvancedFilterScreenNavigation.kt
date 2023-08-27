package br.com.market.core.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.filter.arguments.NumberAdvancedFilterArgs
import br.com.market.core.ui.components.filter.NumberAdvancedFilter
import br.com.market.core.ui.viewmodel.filter.NumberAdvancedFilterViewModel
import com.google.gson.Gson

internal const val numberAdvancedFilterScreenRoute = "numberAdvancedFilterScreen"
const val numberAdvancedFilterScreenNavResultCallbackKey = "numberAdvancedFilterScreenNavResultCallbackKey"
internal const val numberAdvancedFilterArguments = "numberAdvancedFilterArguments"

fun NavGraphBuilder.numberAdvancedFilterScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (Number?) -> Unit
) {
    composable(
        route = "$numberAdvancedFilterScreenRoute?$numberAdvancedFilterArguments={$numberAdvancedFilterArguments}"
    ) {
        val viewModel = hiltViewModel<NumberAdvancedFilterViewModel>()

        NumberAdvancedFilter(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToNumberAdvancedFilterScreen(
    args: NumberAdvancedFilterArgs,
    callback: (String) -> Unit,
) {
    val json = Gson().getAdapter(NumberAdvancedFilterArgs::class.java).toJson(args)

    navigateForResult(
        key = numberAdvancedFilterScreenNavResultCallbackKey,
        route = "$numberAdvancedFilterScreenRoute?$numberAdvancedFilterArguments={$json}",
        callback = callback
    )
}