package br.com.market.core.ui.components.image

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.net.toFile
import java.io.File

class CameraImageSavedCallback(
    private val context: Context,
    private val photoFile: File,
    private val onImageCaptured: (Uri, Boolean) -> Unit,
    private val onImageSavedError: (ImageCaptureException) -> Unit
) : ImageCapture.OnImageSavedCallback {

    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
        val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(savedUri.toFile().extension)

        MediaScannerConnection.scanFile(
            context,
            arrayOf(savedUri.toFile().absolutePath),
            arrayOf(mimeType)
        ) { _, uri ->
            onImageCaptured(uri, false)
        }
    }

    override fun onError(exception: ImageCaptureException) {
        onImageSavedError(exception)
    }
}
