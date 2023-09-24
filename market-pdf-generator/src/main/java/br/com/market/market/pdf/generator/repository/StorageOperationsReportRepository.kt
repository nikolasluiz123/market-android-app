package br.com.market.market.pdf.generator.repository

import br.com.market.localdataaccess.dao.report.StorageOperationsReportDAO
import br.com.market.report.data.BrandReportData
import br.com.market.report.data.CategoryReportData
import br.com.market.report.data.ProductReportData
import br.com.market.report.data.ReportCommonData
import br.com.market.report.data.StorageOperationReportData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StorageOperationsReportRepository @Inject constructor(
    private val reportDAO: StorageOperationsReportDAO
) {

    suspend fun getCommonInformation(): ReportCommonData = withContext(IO) {
        reportDAO.getCommonInformation()
    }

    suspend fun getCategories(): List<CategoryReportData> = withContext(IO) {
        reportDAO.getCategories()
    }

    suspend fun getBrands(categoryId: String): List<BrandReportData> = withContext(IO) {
        reportDAO.getBrands(categoryId)
    }

    suspend fun getProducts(categoryId: String, brandId: String): List<ProductReportData> = withContext(IO) {
        reportDAO.getProducts(categoryId, brandId)
    }

    suspend fun getOperations(productId: String): List<StorageOperationReportData> = withContext(IO) {
        reportDAO.getOperations(productId)
    }

    suspend fun getStorageCount(productId: String): Int = withContext(IO) {
        reportDAO.getStorageCount(productId)
    }
}