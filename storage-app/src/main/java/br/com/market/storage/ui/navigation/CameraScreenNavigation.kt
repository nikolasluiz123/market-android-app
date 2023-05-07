package br.com.market.storage.ui.navigation

import android.net.Uri
import androidx.camera.core.ImageCaptureException
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.CameraView

internal const val cameraScreenRoute = "camera"
internal const val cameraNavResultCallbackKey = "cameraCallbackKey"

fun NavGraphBuilder.cameraGraph(
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    composable(route = cameraScreenRoute) {
        CameraView(
            onImageCaptured = onImageCaptured,
            onError = onError
        )
    }
}

fun NavController.navigateToCameraScreen(navOptions: NavOptions? = null) {
    navigate(route = cameraScreenRoute, navOptions = navOptions)
}