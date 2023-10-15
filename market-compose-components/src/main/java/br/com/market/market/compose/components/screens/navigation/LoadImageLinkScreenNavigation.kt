package br.com.market.market.compose.components.screens.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.extensions.navigateForResult
import br.com.market.market.compose.components.screens.LoadImageLinkScreen
import br.com.market.market.compose.components.screens.viewmodel.LoadImageLinkViewModel

internal const val loadImageLinkScreenRoute = "loadImageLink"
const val loadImageLinkNavResultCallbackKey = "loadImageLinkCallbackKey"

/**
 * Função que realiza as definições da navegação para a
 * tela de Login.
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavGraphBuilder.loadImageLinkGraph(
    onNavigationIconClick: () -> Unit,
    onSaveClick: (ByteArray) -> Unit
) {
    composable(route = loadImageLinkScreenRoute) {
        val loadImageLinkViewModel = hiltViewModel<LoadImageLinkViewModel>()

        LoadImageLinkScreen(
            viewModel = loadImageLinkViewModel,
            onNavigationIconClick = onNavigationIconClick,
            onSaveClick = onSaveClick
        )
    }
}

fun NavController.navigateToLoadImageLinkScreen(callback: (ByteArray) -> Unit, navOptions: NavOptions? = null) {
    navigateForResult(
        key = loadImageLinkNavResultCallbackKey,
        route = loadImageLinkScreenRoute,
        callback = callback,
        navOptions = navOptions
    )
}