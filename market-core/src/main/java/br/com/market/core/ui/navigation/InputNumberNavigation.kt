package br.com.market.core.ui.navigation

import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.filter.arguments.InputNumberArgs
import br.com.market.core.gson.InterfaceAdapter
import br.com.market.core.ui.components.input.InputNumber
import br.com.market.core.ui.viewmodel.input.InputNumberViewModel
import com.google.gson.GsonBuilder

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
        val viewModel = hiltViewModel<InputNumberViewModel>()

        InputNumber(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToInputNumber(
    args: InputNumberArgs,
    callback: (Number?) -> Unit,
) {
    val gson = GsonBuilder()
        .registerTypeAdapter(VisualTransformation::class.java, InterfaceAdapter<VisualTransformation>())
        .create()

    val json = gson.getAdapter(InputNumberArgs::class.java).toJson(args)

    navigateForResult(
        key = numberAdvancedFilterScreenNavResultCallbackKey,
        route = "$numberAdvancedFilterScreenRoute?$numberAdvancedFilterArguments={$json}",
        callback = callback
    )
}