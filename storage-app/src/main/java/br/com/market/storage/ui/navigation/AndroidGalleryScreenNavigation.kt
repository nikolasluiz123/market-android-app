package br.com.market.storage.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.AndroidGallery


fun NavGraphBuilder.androidGalleryGraph(
    navController: NavHostController
) {
    composable(route = StorageAppDestinations.AndroidGallery.route) {
        AndroidGallery(context = LocalContext.current) {

        }

        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }
}