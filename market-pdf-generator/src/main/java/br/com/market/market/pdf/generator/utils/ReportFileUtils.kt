package br.com.market.market.pdf.generator.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.provider.MediaStore
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.enums.EnumFileExtension
import br.com.market.core.extensions.format
import br.com.market.core.extensions.parseToLocalDateTime
import br.com.market.core.utils.FileUtils
import br.com.market.market.pdf.generator.common.ReportFile
import br.com.market.market.pdf.generator.enums.EnumReportDirectory
import java.io.File
import java.io.FileOutputStream

/**
 * Object utilitário contendo funções para auxiliar nas operações
 * com os relatórios PDF.
 *
 * @author Nikolas Luiz Schmitt
 */
object ReportFileUtils {

    /**
     * Função que salva o relatório PDF gerado no diretório específico.
     *
     * @param directory Diretório que será salvo o PDF
     * @param fileName Nome do arquivo
     * @param pdf Objeto usado para escrever o arquivo no diretório especificado
     *
     * @author Nikolas Luiz Schmitt
     */
    fun savePDF(directory: EnumReportDirectory, fileName: String, pdf: PdfDocument) {
        val moduleReportsFolder = getReportsModuleDirectory(directory)
        pdf.writeTo(FileOutputStream(File(moduleReportsFolder, fileName)))
    }

    /**
     * Função que retorna uma lista de [ReportFile] de um diretório específico.
     *
     * @param context Contexto de quem chamou
     * @param reportDirectory Diretório de onde devem ser buscados os relatórios.
     *
     * @author Nikolas Luiz Schmitt
     */
    fun getReportsFromDirectory(context: Context, reportDirectory: String): List<ReportFile> {
        val reports = mutableListOf<ReportFile>()

        val cursor = getCursorFindReportFile(context, reportDirectory)

        cursor?.use {
            val dataIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val displayNameIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)

            while (it.moveToNext()) {
                val path = it.getString(dataIndex)
                val name = FileUtils.getFileName(it.getString(displayNameIndex), EnumFileExtension.PDF_FILE)
                val date = getDateFromReportFileName(it.getString(displayNameIndex))

                reports.add(ReportFile(name, date, path))
            }
        }

        return reports
    }

    /**
     * Função que recupera um [Cursor] para buscar os relatórios PDF.
     *
     * @param context Contexto da chamada
     * @param reportDirectory Diretório onde estão os relatórios
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getCursorFindReportFile(context: Context, reportDirectory: String): Cursor? {
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Files.getContentUri(FileUtils.EXTERNAL_FILES_VOLUME_NAME)

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME
        )

        val documentFolderPath = FileUtils.getDocumentsDirectory().path
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE ?"
        val selectionArgs = arrayOf("$documentFolderPath/${reportDirectory}%")

        return contentResolver.query(uri, projection, selection, selectionArgs, null)
    }

    /**
     * Função que recupera a data contida no nome do arquivo
     *
     * @param fileName Nome do arquivo
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getDateFromReportFileName(fileName: String): String {
        val split = fileName.split(FileUtils.FILE_NAME_DATE_SEPARATOR)
        return split[1]
            .replace(EnumFileExtension.PDF_FILE.value, "")
            .parseToLocalDateTime(EnumDateTimePatterns.DATE_TIME_FILE_NAME)
            .format(EnumDateTimePatterns.DATE_TIME)
    }

    /**
     * Função que recupera o diretório de relatórios.
     *
     * @param directory Caminho
     *
     * @author Nikolas Luiz Schmitt
     */
    private fun getReportsModuleDirectory(directory: EnumReportDirectory): File {
        val moduleReportsFolder = File(FileUtils.getDocumentsDirectory(), directory.path)

        if (!moduleReportsFolder.exists()) {
            moduleReportsFolder.mkdirs()
        }

        return moduleReportsFolder
    }
}