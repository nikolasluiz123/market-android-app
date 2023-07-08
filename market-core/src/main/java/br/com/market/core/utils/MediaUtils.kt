package br.com.market.core.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

/**
 * Object que contem funções utilitárias relacionadas a media
 *
 * @author Nikolas Luiz Schmitt
 */
object MediaUtils {

    @Composable
    fun openCameraLauncher(onResult: (Boolean) -> Unit) = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture(), onResult)

    @Composable
    fun openGalleryLauncher(onResult: (Uri?) -> Unit) = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia(), onResult)
}