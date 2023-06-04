package br.com.market.core.extensions

import android.content.Context
import android.net.Uri
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import br.com.market.core.R
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Função para obter um File que aponta para o diretório onde
 * ficarão as imagens da câmera, galeria e afins.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Context.getOutputDirectory(): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(it, this.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else this.filesDir
}

/**
 * Função utilizada para recuperar o provedor da câmera,
 * utilizado para associar ela ao ciclo de vida.
 *
 * @author Nikolas Luiz Schmitt
 */
suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

/**
 * Função que lê uma [Uri] e transforma em um [ByteArray]
 *
 * @param uri Objeto do tipo [Uri] que será lido
 *
 * @author Nikolas Luiz Schmitt
 */
@Throws(IOException::class)
fun Context.readBytes(uri: Uri): ByteArray? =
    contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }