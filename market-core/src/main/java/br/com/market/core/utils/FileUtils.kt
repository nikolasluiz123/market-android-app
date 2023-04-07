package br.com.market.core.utils

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    /**
     * Função para criar um arquivo em uma pasta específica seguindo um padrão
     * de nomenclatura contendo a data atual.
     *
     * @param baseFolder
     * @param extension
     */
    fun createFile(baseFolder: File, extension: String): File {
        return File(
            baseFolder,
            SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis()) + extension
        )
    }

    /**
     * Função para recuperar as opções de armazenamento de imagens capturadas
     * da câmera.
     *
     * @param lensFacing
     * @param photoFile
     *
     */
    fun getOutputFileOptions(
        lensFacing: Int,
        photoFile: File
    ): ImageCapture.OutputFileOptions {

        val metadata = ImageCapture.Metadata().apply {
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }

        return ImageCapture.OutputFileOptions.Builder(photoFile).setMetadata(metadata).build()
    }

}

