package br.com.market.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val COMPRESSED_BYTE_ARRAY_HEADER = "COMPRESSED:"

/**
 * Função que lê uma [Uri] e transforma em um [ByteArray]
 *
 * @param uri Objeto do tipo [Uri] que será lido
 *
 * @author Nikolas Luiz Schmitt
 */
@Throws(IOException::class)
fun Context.readBytes(uri: Uri): ByteArray? = contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }

/**
 * Função que lê os bytes da [Uri], redimensiona e comprime.
 *
 * @see readBytes
 * @see resizeImage
 *
 * @param uri Uri da imagem que será lida
 * @param width Largura que a imagem deve possuir
 * @param height Altura que a imagem deve possuir
 *
 * @author Nikolas Luiz Schmitt
 */
@Throws(Exception::class)
fun Context.processBytesOfImage(uri: Uri, width: Int = 1080, height: Int = 1080): ByteArray? {
    return readBytes(uri)?.resizeImage(width, height)
}

/**
 * Função que redimensiona a imagem através dos seus bytes.
 *
 * @param maxWidth Largura que a imagem deve possuir
 * @param maxHeight Altura que a imagem deve possuir
 *
 *  @author Nikolas Luiz Schmitt
 */
@Throws(Exception::class)
fun ByteArray.resizeImage(maxWidth: Int, maxHeight: Int): ByteArray? {
    val options = BitmapFactory.Options().also { bitmapOptions ->
        bitmapOptions.inJustDecodeBounds = true
        bitmapOptions.inSampleSize = bitmapOptions.calculateInSampleSize(maxWidth, maxHeight)
        bitmapOptions.inJustDecodeBounds = false
    }

    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size, options)

    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, options.outWidth, options.outHeight, false)

    val outputStream = ByteArrayOutputStream()

    // Usando o WEBP depreciado pois ele é o único que faz uma compressão descente na versão android mínima do projeto.
    @Suppress("DEPRECATION")
    resizedBitmap.compress(Bitmap.CompressFormat.WEBP, if(resizedBitmap.byteCount > (1024 * 1024)) 50 else 100, outputStream)

    return outputStream.toByteArray()
}

/**
 * Função para calcular o tamanho da imagem de amostra, basicamente,
 * um número X de vezes menor que o original dependendo do inteiro retornado.
 *
 * Exemplo: Se o retorno for 4, o tamanho da imagem de amostra é 1/4 do tamanho
 * da imagem original.
 *
 * @see BitmapFactory.Options.inSampleSize
 *
 * @param reqWidth Largura da imagem
 * @param reqHeight Altura da imagem
 *
 * @author Nikolas Luiz Schmitt
 */
private fun BitmapFactory.Options.calculateInSampleSize(reqWidth: Int, reqHeight: Int): Int {
    val (width: Int, height: Int) = this.run { outWidth to outHeight }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

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