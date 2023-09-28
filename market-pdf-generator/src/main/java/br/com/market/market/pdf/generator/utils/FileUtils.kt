package br.com.market.market.pdf.generator.utils

import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.format
import br.com.market.core.extensions.parseToLocalDateTime
import br.com.market.market.pdf.generator.common.ReportFile
import br.com.market.market.pdf.generator.enums.EnumReportDirectory
import java.io.File
import java.io.FileOutputStream

private const val EXTERNAL_FILES_VOLUME_NAME = "external"
private const val FILE_PROVIDER = "br.com.market.storage.fileprovider"

object FileUtils {

    fun savePDF(folder: EnumReportDirectory, fileName: String, pdf: PdfDocument) {
        val moduleReportsFolder = getReportsModuleDirectory(folder)
        pdf.writeTo(FileOutputStream(File(moduleReportsFolder, fileName)))
    }

    fun getReportsFromFolder(context: Context, reportDirectory: String): List<ReportFile> {
        val reports = mutableListOf<ReportFile>()

        val cursor = getCursorFindReportFile(context, reportDirectory)

        cursor?.use {
            val dataIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val displayNameIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)

            while (it.moveToNext()) {
                val path = it.getString(dataIndex)
                val name = getFileName(it.getString(displayNameIndex))
                val date = getDateFromReportFileName(it.getString(displayNameIndex))

                reports.add(ReportFile(name, date, path))
            }
        }

        return reports
    }

    fun shareFile(context: Context, path: String) {
        val fileUri = getUriReportForShare(context, path)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtensionFromFile(path))

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = mimeType
            clipData = ClipData.newRawUri("", fileUri);
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, "Compartilhar Relatório"),
            null)
    }

    private fun getCursorFindReportFile(context: Context, reportDirectory: String): Cursor? {
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Files.getContentUri(EXTERNAL_FILES_VOLUME_NAME)

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME
        )

        val documentFolderPath = getDocumentsDirectory().path
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE ?"
        val selectionArgs = arrayOf("$documentFolderPath/${reportDirectory}%")

        return contentResolver.query(uri, projection, selection, selectionArgs, null)
    }

    private fun getDateFromReportFileName(fileName: String): String {
        val split = fileName.split("-")
        return split[1]
            .replace(".pdf", "")
            .parseToLocalDateTime(EnumDateTimePatterns.DATE_TIME_FILE_NAME)
            .format(EnumDateTimePatterns.DATE_TIME)
    }

    private fun getFileName(fullFileName: String): String {
        return fullFileName.split("-")[0].replace(".pdf", "")
    }

    private fun getReportsModuleDirectory(folder: EnumReportDirectory): File {
        val moduleReportsFolder = File(getDocumentsDirectory(), folder.path)

        if (!moduleReportsFolder.exists()) {
            moduleReportsFolder.mkdirs()
        }

        return moduleReportsFolder
    }

    private fun getDocumentsDirectory(): File {
        val externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        if (!externalStorage.exists()) {
            externalStorage.mkdirs()
        }

        return externalStorage
    }

    private fun getExtensionFromFile(path: String): String {
        return path.split(".") [1]
    }

    private fun getUriReportForShare(context: Context, path: String): Uri {
        val file = File(path)
        return FileProvider.getUriForFile(context, FILE_PROVIDER, file, getFileName(path))
    }
}