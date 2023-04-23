package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.LoadImageLinkScreen
import br.com.market.core.ui.viewmodel.LoadImageLinkViewModel

internal const val loadImageLinkScreenRoute = "loadImageLink"

/**
 * Função que realiza as definições da navegação para a
 * tela de Login.
 *
 * @param navController Controlador da navegação do compose.
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavGraphBuilder.loadImageLinkGraph(
    navController: NavHostController
) {
    composable(route = loadImageLinkScreenRoute) {
        val loadImageLinkViewModel = hiltViewModel<LoadImageLinkViewModel>()

        LoadImageLinkScreen(
            viewModel = loadImageLinkViewModel,
            onNavigationIconClick = {
                navController.popBackStack()
            }
        )
    }
}

fun NavController.navigateToLoadImageLinkScreen(navOptions: NavOptions? = null) {
    navigate(route = loadImageLinkScreenRoute, navOptions = navOptions)
}