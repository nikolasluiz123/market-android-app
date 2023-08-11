package br.com.market.core.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.filter.AdvancedFilterArgs
import br.com.market.core.ui.components.filter.TextAdvancedFilter
import br.com.market.core.ui.viewmodel.filter.TextAdvancedFilterViewModel
import com.google.gson.Gson

internal const val textAdvancedFilterScreenRoute = "textAdvancedFilterScreen"
const val textAdvancedFilterScreenNavResultCallbackKey = "textAdvancedFilterScreenNavResultCallbackKey"
internal const val textAdvancedFilterArguments = "textAdvancedFilterArguments"

fun NavGraphBuilder.textAdvancedFilterScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    composable(
        route = "$textAdvancedFilterScreenRoute?$textAdvancedFilterArguments={$textAdvancedFilterArguments}"
    ) {
        val viewModel = hiltViewModel<TextAdvancedFilterViewModel>()

        TextAdvancedFilter(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToTextAdvancedFilterScreen(
    args: AdvancedFilterArgs,
    callback: (String) -> Unit,
) {
    val json = Gson().getAdapter(AdvancedFilterArgs::class.java).toJson(args)

    navigateForResult(
        key = textAdvancedFilterScreenNavResultCallbackKey,
        route = "$textAdvancedFilterScreenRoute?$textAdvancedFilterArguments={$json}",
        callback = callback
    )
}