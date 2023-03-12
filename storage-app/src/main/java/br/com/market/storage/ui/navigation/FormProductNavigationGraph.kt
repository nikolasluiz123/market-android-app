package br.com.market.storage.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.market.storage.ui.screens.formproduct.FormProductScreen
import br.com.market.storage.ui.viewmodels.FormProductViewModel
import br.com.market.storage.ui.viewmodels.LoginViewModel

/**
 * Função que realiza as definições da navegação para a
 * tela de Criação / Editação do Produto e das Marcas.
 *
 * @param navController Controlador da navegação do compose.
 *
 * @author Nikolas Luiz Schmitt
 */
fun NavGraphBuilder.formProductGraph(
    navController: NavHostController
) {
    composable(
        route = "${StorageAppDestinations.FormProduct.route}?productId={productId}",
        arguments = listOf(
            navArgument("productId") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        val formProductViewModel = hiltViewModel<FormProductViewModel>()
        val loginViewModel = hiltViewModel<LoginViewModel>()

        FormProductScreen(
            viewModel = formProductViewModel,
            onLogoutClick = {
                loginViewModel.logout()

                navController.inclusiveNavigation(
                    originRoute = StorageAppDestinations.StorageProducts.route,
                    destinyRoute = StorageAppDestinations.Login.route
                )
            },
            onBackClick = {
                navController.popBackStack()
            },
            onSuccessDeleteProduct = {
                navController.popBackStack()
            }
        )
    }
}