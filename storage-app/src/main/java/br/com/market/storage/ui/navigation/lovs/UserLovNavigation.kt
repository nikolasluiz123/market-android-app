package br.com.market.storage.ui.navigation.lovs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.market.core.ui.navigation.navigateForResult
import br.com.market.storage.ui.component.lov.UserLov
import br.com.market.storage.ui.viewmodels.user.UserLovViewModel

internal const val userLovRoute = "userLov"
internal const val userLovNavResultCallbackKey = "userLovCallbackKey"

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