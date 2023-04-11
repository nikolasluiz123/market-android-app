package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.LoadImageLinkScreen
import br.com.market.core.ui.viewmodel.LoadImageLinkViewModel

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
    composable(route = StorageAppDestinations.LoadImageLink.route) {
        val loadImageLinkViewModel = hiltViewModel<LoadImageLinkViewModel>()

        LoadImageLinkScreen(
            viewModel = loadImageLinkViewModel,
            onNavigationIconClick = {
                navController.popBackStack()
            }
        )
    }
}