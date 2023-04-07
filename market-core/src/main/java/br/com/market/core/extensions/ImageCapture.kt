package br.com.market.core.extensions

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import br.com.market.core.ui.components.image.CameraImageSavedCallback
import br.com.market.core.utils.FileUtils
import java.util.concurrent.Executors

fun ImageCapture.takePicture(
    context: Context,
    lensFacing: Int,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val outputDirectory = context.getOutputDirectory()
    val photoFile = FileUtils.createFile(baseFolder = outputDirectory, extension = ".jpg")
    val outputFileOptions = FileUtils.getOutputFileOptions(lensFacing, photoFile)

    this.takePicture(
        outputFileOptions,
        Executors.newSingleThreadExecutor(),
        CameraImageSavedCallback(context, photoFile, onImageCaptured, onError)
    )
}