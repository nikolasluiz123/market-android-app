package br.com.market.storage.ui.navigation

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.market.core.ui.components.image.CameraView


fun NavGraphBuilder.cameraGraph(
    navController: NavHostController
) {
    composable(route = StorageAppDestinations.Camera.route) {
        CameraView(
            onImageCaptured = { uri, fromGallery ->
                Log.i("TAG", "cameraGraph: Bateu a Foto")
            }, onError = { imageCaptureException ->

            }
        )
    }
}