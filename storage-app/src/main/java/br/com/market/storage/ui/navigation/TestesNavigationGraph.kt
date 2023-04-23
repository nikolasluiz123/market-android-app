package br.com.market.storage.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.storage.ui.screens.testes.TestesBottomAppBar

fun NavGraphBuilder.testesGraph(
    navController: NavHostController
) {
    composable(route = StorageAppDestinations.Testes.route) {
        TestesBottomAppBar()
    }
}