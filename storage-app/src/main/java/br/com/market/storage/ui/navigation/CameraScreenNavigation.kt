package br.com.market.storage.ui.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.CameraView

internal const val cameraScreenRoute = "camera"

fun NavGraphBuilder.cameraGraph() {
    composable(route = cameraScreenRoute) {
        CameraView(
            onImageCaptured = { uri, fromGallery ->
                Log.i("TAG", "cameraGraph: Bateu a Foto")
            }, onError = { imageCaptureException ->

            }
        )
    }
}

fun NavController.navigateToCameraScreen(navOptions: NavOptions? = null) {
    navigate(route = cameraScreenRoute, navOptions = navOptions)
}