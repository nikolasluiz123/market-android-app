package br.com.market.core.utils

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import br.com.market.core.enums.EnumFileExtension
import java.io.File

/**
 * Object utilitário contendo funções para auxiliar nas operações
 * com arquivos
 *
 * @author Nikolas Luiz Schmitt
 */
object FileUtils {
    private const val FILE_EXTENSION_SEPARATOR = "."
    private const val FILE_PATH_SEPARATOR = "/"
    private const val FILE_PROVIDER = "br.com.market.storage.fileprovider"

    const val FILE_NAME_DATE_SEPARATOR = "-"
    const val EXTERNAL_FILES_VOLUME_NAME = "external"

    /**
     * Função que permite compartilhar arquivos com outros apps.
     *
     * @param context Contexto da chamada
     * @param path Caminho para o arquivo
     * @param extension Extensão do arquivo
     * @param title Título exibido na tela
     *
     * @author Nikolas Luiz Schmitt
     */
    fun shareFile(context: Context, path: String, extension: EnumFileExtension, title: String) {
        val fileUri = getUriFromPath(context, path, extension)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtensionFromFile(path))

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = mimeType
            clipData = ClipData.newRawUri("", fileUri);
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        ContextCompat.startActivity(context, Intent.createChooser(intent, title), null)
    }

    /**
     * Função para recuperar a extensão do arquivo.
     *
     * @param path Caminho do arquivo.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun getExtensionFromFile(path: String): String {
        return path.split(FILE_EXTENSION_SEPARATOR)[1]
    }

    /**
     * Função que recupera a Uri de um arquivo pelo caminho
     * utilizando o FileProvider
     *
     * @param context Contexto da chamada da função
     * @param path Caminho do arquivo
     * @param extension Extensão do arquivo
     *
     * @author Nikolas Luiz Schmitt
     */
    fun getUriFromPath(context: Context, path: String, extension: EnumFileExtension): Uri {
        val file = File(path)
        return FileProvider.getUriForFile(context, FILE_PROVIDER, file, getFileName(path, extension))
    }

    /**
     * Função para recuperar o nome do arquivo a partir do caminho completo.
     * A função considera que o padrão de nomenclatura seja, por exemplo: relatorio_dd_MM_yyyy_HHmmss
     *
     * @param fullFileName Caminho completo do arquivo
     * @param extension Extensão do arquivo
     *
     * @author Nikolas Luiz Schmitt
     */
    fun getFileName(fullFileName: String, extension: EnumFileExtension): String {
        val nameWithoutPath = getFileNameWithoutPath(fullFileName)
        return nameWithoutPath.split(FILE_NAME_DATE_SEPARATOR)[0].replace(extension.value, "")
    }

    /**
     * Função para recuperar o nome do arquivo, removendo qualquer outra coisa dele,
     * por exemplo, o caminho.
     *
     * @param fullFileName Nome completo do arquivo
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getFileNameWithoutPath(fullFileName: String) = if (fullFileName.contains(FILE_PATH_SEPARATOR)) {
        fullFileName.substring(fullFileName.lastIndexOf(FILE_PATH_SEPARATOR) + 1)
    } else {
        fullFileName
    }

    /**
     * Função que retorna o diretório Documents.
     *
     * @see getPublicEnvironmentDirectory
     *
     * @author Nikolas Luiz Schmitt
     */
    fun getDocumentsDirectory(): File {
        return getPublicEnvironmentDirectory(Environment.DIRECTORY_DOCUMENTS)
    }

    /**
     * Função que retorna um diretório público. Caso não existir ele será criado.
     *
     * @param environment Diretório que deseja, utilize [Environment.DIRECTORY_DOCUMENTS], por exemplo.
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getPublicEnvironmentDirectory(environment: String): File {
        val externalStorage = Environment.getExternalStoragePublicDirectory(environment)

        if (!externalStorage.exists()) {
            externalStorage.mkdirs()
        }

        return externalStorage
    }

    fun deleteFile(path: String) {
        File(path).delete()
    }
}