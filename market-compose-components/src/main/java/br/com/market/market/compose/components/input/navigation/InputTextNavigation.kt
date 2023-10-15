package br.com.market.market.compose.components.input.navigation

import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.core.gson.adapter.InterfaceAdapter
import br.com.market.core.inputs.arguments.InputArgs
import br.com.market.market.compose.components.input.viewmodel.InputTextViewModel
import br.com.market.market.compose.components.input.InputText
import com.google.gson.GsonBuilder

internal const val inputTextScreenRoute = "inputTextScreenRoute"
const val inputTextNavResultCallbackKey = "inputTextScreenNavResultCallbackKey"
internal const val inputTextArguments = "inputTextArguments"

fun NavGraphBuilder.inputText(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (String?) -> Unit
) {
    composable(
        route = "$inputTextScreenRoute?$inputTextArguments={$inputTextArguments}"
    ) {
        val viewModel = hiltViewModel<InputTextViewModel>()

        InputText(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToInputText(
    args: InputArgs,
    callback: (String) -> Unit,
) {
    val gson = GsonBuilder()
        .registerTypeAdapter(VisualTransformation::class.java, InterfaceAdapter<VisualTransformation>())
        .create()

    val json = gson.getAdapter(InputArgs::class.java).toJson(args)

    navigateForResult(
        key = inputTextNavResultCallbackKey,
        route = "$inputTextScreenRoute?$inputTextArguments={$json}",
        callback = callback
    )
}