package br.com.market.core.extensions

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

/**
 * Função que cria um arquivo de imagem no diretório e chama
 * a câmera do android para que a foto batida seja salva no
 * arquivo criado.
 *
 * @param pickCamera Launcher que será executado passando o arquivo criado
 * @param onImageUriLoaded Callback executado quando a imagem for criada
 *
 * @author Nikolas Luiz Schmitt
 */
fun Context.openCamera(
    pickCamera: ManagedActivityResultLauncher<Uri, Boolean>,
    onImageUriLoaded: (Uri) -> Unit
) {
    val imageFile = createImageFile()

    val imageUri = FileProvider.getUriForFile(this, "${this.packageName}.fileprovider", imageFile)

    onImageUriLoaded(imageUri)
    pickCamera.launch(imageUri)
}

/**
 * Função que criar um arquivo de imagem temporário no diretório
 * externo.
 *
 * @author Nikolas Luiz Schmitt
 */
fun Context.createImageFile(): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

    return File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )
}