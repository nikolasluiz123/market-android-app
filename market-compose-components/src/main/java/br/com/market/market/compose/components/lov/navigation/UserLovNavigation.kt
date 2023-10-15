package br.com.market.market.compose.components.lov.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.market.compose.components.lov.UserLov
import br.com.market.market.compose.components.lov.viewmodel.UserLovViewModel

internal const val userLovRoute = "userLov"
const val userLovNavResultCallbackKey = "userLovCallbackKey"

fun NavGraphBuilder.userLov(
    onItemClick: (Pair<String, String>) -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = userLovRoute) {
        val userViewModel = hiltViewModel<UserLovViewModel>()

        UserLov(
            viewModel = userViewModel,
            onItemClick = onItemClick,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToUserLov(callback: (Pair<String, String>) -> Unit) {
    navigateForResult(
        key = userLovNavResultCallbackKey,
        route = userLovRoute,
        callback = callback
    )
}