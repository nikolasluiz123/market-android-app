package br.com.market.market.compose.components.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.core.inputs.arguments.InputPasswordArgs
import br.com.market.market.compose.components.input.viewmodel.InputPasswordViewModel
import com.google.gson.Gson

internal const val inputPasswordScreenRoute = "inputPasswordScreenRoute"
const val inputPasswordNavResultCallbackKey = "inputPasswordNavResultCallbackKey"
internal const val inputPasswordArguments = "inputPasswordArguments"

fun NavGraphBuilder.inputPassword(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (String?) -> Unit
) {
    composable(
        route = "$inputPasswordScreenRoute?$inputPasswordArguments={$inputPasswordArguments}"
    ) {
        val viewModel = hiltViewModel<InputPasswordViewModel>()

        br.com.market.market.compose.components.input.InputPassword(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onConfirmClick = onConfirmClick,
            onCancelClick = onCancelClick
        )
    }
}

fun NavController.navigateToInputPassword(
    args: InputPasswordArgs,
    callback: (String) -> Unit,
) {
    val json = Gson().getAdapter(InputPasswordArgs::class.java).toJson(args)

    navigateForResult(
        key = inputPasswordNavResultCallbackKey,
        route = "$inputPasswordScreenRoute?$inputPasswordArguments={$json}",
        callback = callback
    )
}