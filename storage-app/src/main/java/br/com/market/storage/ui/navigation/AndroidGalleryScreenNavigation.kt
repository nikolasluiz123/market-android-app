package br.com.market.storage.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.AndroidGallery

internal const val androidGalleryScreenRoute = "androidGallery"

fun NavGraphBuilder.androidGalleryGraph(
    onAfterShowGallery: () -> Unit
) {
    composable(route = androidGalleryScreenRoute) {
        AndroidGallery(context = LocalContext.current) {

        }

        LaunchedEffect(Unit) {
            onAfterShowGallery()
        }
    }
}

fun NavController.navigateToAndroidGalleryScreen(navOptions: NavOptions? = null) {
    navigate(route = androidGalleryScreenRoute, navOptions = navOptions)
}