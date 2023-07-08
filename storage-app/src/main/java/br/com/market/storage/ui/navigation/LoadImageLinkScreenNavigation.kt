package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.LoadImageLinkScreen
import br.com.market.core.ui.viewmodel.LoadImageLinkViewModel

internal const val loadImageLinkScreenRoute = "loadImageLink"
internal const val loadImageLinkNavResultCallbackKey = "loadImageLinkCallbackKey"

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