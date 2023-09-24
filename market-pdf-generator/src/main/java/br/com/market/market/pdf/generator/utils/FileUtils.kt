package br.com.market.market.pdf.generator.utils

import android.graphics.pdf.PdfDocument
import android.os.Environment
import br.com.market.market.pdf.generator.enums.EnumReportDirectory
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun savePDF(folder: EnumReportDirectory, fileName: String, pdf: PdfDocument) {
        val moduleReportsFolder = createReportsModuleDirectory(folder)
        pdf.writeTo(FileOutputStream(File(moduleReportsFolder, fileName)))
    }

    private fun createReportsModuleDirectory(folder: EnumReportDirectory): File {
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
}