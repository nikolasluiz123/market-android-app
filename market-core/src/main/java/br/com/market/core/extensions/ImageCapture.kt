package br.com.market.core.extensions

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import br.com.market.core.ui.components.image.CameraImageSavedCallback
import br.com.market.core.utils.FileUtils
import java.util.concurrent.Executors

/**
 * Função responsável por criar um File no diretório definido
 * por [getOutputDirectory] e delegar para [ImageCapture.takePicture]
 *
 * @param context Contexto da chamada
 * @param lensFacing Câmera Frontal ou Traseira
 * @param onImageCaptured Callback para quando a imagem for capturada
 * @param onError Callback para quando ocorrer um erro.
 *
 * @see ImageCapture.takePicture
 *
 * @author Nikolas Luiz Schmitt
 */
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