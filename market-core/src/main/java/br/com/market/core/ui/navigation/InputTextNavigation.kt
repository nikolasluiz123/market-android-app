package br.com.market.core.ui.navigation

import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.filter.arguments.InputArgs
import br.com.market.core.gson.InterfaceAdapter
import br.com.market.core.ui.components.input.InputText
import br.com.market.core.ui.viewmodel.input.InputTextViewModel
import com.google.gson.GsonBuilder

internal const val inputTextScreenRoute = "inputTextScreenRoute"
const val inputTextScreenNavResultCallbackKey = "inputTextScreenNavResultCallbackKey"
internal const val inputTextArguments = "inputTextArguments"

fun NavGraphBuilder.inputTextScreen(
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
        key = inputTextScreenNavResultCallbackKey,
        route = "$inputTextScreenRoute?$inputTextArguments={$json}",
        callback = callback
    )
}