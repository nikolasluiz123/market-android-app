package br.com.market.market.compose.components.navigation

import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.core.gson.adapter.InterfaceAdapter
import br.com.market.core.inputs.arguments.InputNumberArgs
import br.com.market.market.compose.components.input.viewmodel.InputNumberViewModel
import com.google.gson.GsonBuilder

internal const val numberAdvancedFilterScreenRoute = "numberAdvancedFilterScreen"
const val inputNumberNavResultCallbackKey = "numberAdvancedFilterScreenNavResultCallbackKey"
internal const val numberAdvancedFilterArguments = "numberAdvancedFilterArguments"

fun NavGraphBuilder.inputNumber(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (Number?) -> Unit
) {
    composable(
        route = "$numberAdvancedFilterScreenRoute?$numberAdvancedFilterArguments={$numberAdvancedFilterArguments}"
    ) {
        val viewModel = hiltViewModel<InputNumberViewModel>()

        br.com.market.market.compose.components.input.InputNumber(
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
        key = inputNumberNavResultCallbackKey,
        route = "$numberAdvancedFilterScreenRoute?$numberAdvancedFilterArguments={$json}",
        callback = callback
    )
}