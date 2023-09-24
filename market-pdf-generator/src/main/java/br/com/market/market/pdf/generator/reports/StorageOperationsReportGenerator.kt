package br.com.market.market.pdf.generator.reports

import android.content.Context
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.Page
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.format
import br.com.market.enums.EnumOperationType
import br.com.market.market.pdf.generator.R
import br.com.market.market.pdf.generator.enums.EnumPageSize
import br.com.market.market.pdf.generator.enums.EnumReportDirectory
import br.com.market.market.pdf.generator.extensions.drawLineInPosition
import br.com.market.market.pdf.generator.extensions.drawTableCellValue
import br.com.market.market.pdf.generator.extensions.drawTextInPosition
import br.com.market.market.pdf.generator.extensions.formatQuantityIn
import br.com.market.market.pdf.generator.extensions.splitText
import br.com.market.market.pdf.generator.repository.StorageOperationsReportRepository
import br.com.market.market.pdf.generator.utils.FileUtils
import br.com.market.market.pdf.generator.utils.Margins.MARGIN_16
import br.com.market.market.pdf.generator.utils.Margins.MARGIN_24
import br.com.market.market.pdf.generator.utils.Margins.MARGIN_30
import br.com.market.market.pdf.generator.utils.Margins.MARGIN_60
import br.com.market.market.pdf.generator.utils.Margins.MARGIN_8
import br.com.market.market.pdf.generator.utils.Paints
import br.com.market.market.pdf.generator.utils.Position
import br.com.market.report.data.BrandReportData
import br.com.market.report.data.CategoryReportData
import br.com.market.report.data.ProductReportData
import br.com.market.report.data.ReportCommonData
import br.com.market.report.data.StorageOperationReportData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class StorageOperationsReportGenerator @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: StorageOperationsReportRepository
) : BaseReportGenerator(context) {

    override suspend fun generateReport() {
        val document = PdfDocument()

        try {
            val commonInformation = repository.getCommonInformation()


            repository.getCategories().forEachIndexed { index, category ->
                repository.getBrands(category.categoryId).forEach { brand ->
                    repository.getProducts(category.categoryId, brand.brandId).forEach { product ->
                        val pageInfo = PdfDocument.PageInfo.Builder(EnumPageSize.A4.width, EnumPageSize.A4.height, index + 1).create()
                        var page = document.startPage(pageInfo)

                        val headerPosition = drawReportHeader(page, commonInformation)
                        val footerPosition = drawReportFooter(page, commonInformation)

                        val productInfoPosition = drawProductInfo(
                            page = page,
                            headerPosition = headerPosition,
                            category = category,
                            brand = brand,
                            product = product
                        )

                        page = drawTableOperations(
                            product = product,
                            page = page,
                            productInfoPosition = productInfoPosition,
                            footerPosition = footerPosition,
                            document = document,
                            pageInfo = pageInfo,
                            headerPosition = headerPosition,
                            commonInformation = commonInformation,
                        )

                        document.finishPage(page)
                    }
                }

            }
            val actualDateFile = LocalDateTime.now().format(EnumDateTimePatterns.DATE_TIME_FILE_NAME)
            FileUtils.savePDF(
                EnumReportDirectory.REPORT_DIRECTORY_STORAGE_OPERATIONS,
                "relatorio_operacoes-$actualDateFile.pdf",
                document
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            document.close()
        }
    }

    private suspend fun drawProductInfo(
        page: Page,
        headerPosition: Position,
        category: CategoryReportData,
        brand: BrandReportData,
        product: ProductReportData
    ): Position {
        val columnWidthCategory = 150f
        val columnWidthBrand = 150f
        val columnWidthProduct = 234f

        val labelCategoryPosition = Position(
            axisX = headerPosition.axisX,
            axisY = headerPosition.axisY
        )
        val labelCategory = context.getString(R.string.storage_operations_report_label_category)
        page.canvas.drawTextInPosition(
            text = labelCategory,
            position = labelCategoryPosition,
            paint = Paints.defaultLabelPaint
        )

        val categoryPosition = Position(
            axisX = labelCategoryPosition.axisX,
            axisY = labelCategoryPosition.axisY + MARGIN_16
        )
        page.canvas.drawTextInPosition(
            text = category.categoryName,
            position = categoryPosition,
            paint = Paints.defaultValuePaint,
            columnWidth = columnWidthCategory
        )

        val labelBrandPosition = Position(
            axisX = columnWidthCategory + MARGIN_30,
            axisY = labelCategoryPosition.axisY
        )
        page.canvas.drawTextInPosition(
            text = context.getString(R.string.storage_operations_report_label_brand),
            position = labelBrandPosition,
            paint = Paints.defaultLabelPaint
        )

        val brandPosition = Position(
            axisX = labelBrandPosition.axisX,
            axisY = labelBrandPosition.axisY + MARGIN_16
        )
        page.canvas.drawTextInPosition(
            text = brand.brandName,
            position = brandPosition,
            paint = Paints.defaultValuePaint,
            columnWidth = columnWidthBrand
        )

        val labelProductPosition = Position(
            axisX = columnWidthCategory + columnWidthBrand + MARGIN_30,
            axisY = labelBrandPosition.axisY
        )
        page.canvas.drawTextInPosition(
            text = context.getString(R.string.storage_operations_report_label_product),
            position = labelProductPosition,
            paint = Paints.defaultLabelPaint
        )

        val productPosition = Position(
            axisX = labelProductPosition.axisX,
            axisY = labelProductPosition.axisY + MARGIN_16
        )
        page.canvas.drawTextInPosition(
            text = "${product.productName} ${product.productQuantity.formatQuantityIn(product.quantityUnit)}",
            position = productPosition,
            paint = Paints.defaultValuePaint,
            columnWidth = columnWidthProduct
        )

        val values = listOf(
            Pair(category.categoryName, columnWidthCategory),
            Pair(brand.brandName, columnWidthBrand),
            Pair(product.productName, columnWidthProduct)
        )

        val maxValue = values.maxBy { it.first.splitText(Paints.defaultValuePaint, it.second).size }
        val linesMaxValue = maxValue.first.splitText(Paints.defaultValuePaint, maxValue.second).size

        return Position(
            axisX = categoryPosition.axisX,
            axisY = categoryPosition.axisY + (MARGIN_16 * linesMaxValue)
        )
    }

    private suspend fun drawTableOperations(
        product: ProductReportData,
        page: Page,
        productInfoPosition: Position,
        footerPosition: Position,
        document: PdfDocument,
        pageInfo: PdfDocument.PageInfo,
        headerPosition: Position,
        commonInformation: ReportCommonData,
    ): Page {

        val operations = repository.getOperations(product.productId)
        val tableOperationsColumnWidth = ((page.info.pageWidth - MARGIN_60) / 4).toFloat()
        var axisXHeader = MARGIN_30.toFloat()
        var axisYHeader = productInfoPosition.axisY + MARGIN_30
        val maxHeightTable = sqrt((footerPosition.axisX - productInfoPosition.axisX).pow(2) + (footerPosition.axisY - productInfoPosition.axisY).pow(2))

        var newPage = page
        var newPageInfo = pageInfo

        drawTableOperationsHeader(
            canvas = newPage.canvas,
            axisX = axisXHeader,
            axisY = axisYHeader,
            onNextColumn = {
                axisXHeader += tableOperationsColumnWidth
                axisXHeader
            },
            onFinish = {
                axisYHeader += MARGIN_24
            }
        )

        operations.forEach { data ->
            if (axisYHeader > maxHeightTable) {
                document.finishPage(newPage)
                newPageInfo = PdfDocument.PageInfo.Builder(EnumPageSize.A4.width, EnumPageSize.A4.height, newPageInfo.pageNumber + 1).create()
                newPage = document.startPage(newPageInfo)

                drawReportHeader(newPage, commonInformation)
                drawReportFooter(newPage, commonInformation)

                axisXHeader = MARGIN_30.toFloat()
                axisYHeader = headerPosition.axisY

                drawTableOperationsHeader(
                    canvas = newPage.canvas,
                    axisX = axisXHeader,
                    axisY = axisYHeader,
                    onNextColumn = {
                        axisXHeader += tableOperationsColumnWidth
                        axisXHeader
                    },
                    onFinish = {
                        axisYHeader += MARGIN_24
                    }
                )
            }

            axisXHeader = MARGIN_30.toFloat()

            newPage.canvas.drawTableCellValue(data.datePrevision?.format(EnumDateTimePatterns.DATE_TIME), axisXHeader, axisYHeader) {
                axisXHeader += tableOperationsColumnWidth
            }

            newPage.canvas.drawTableCellValue(data.dateRealization.format(EnumDateTimePatterns.DATE_TIME), axisXHeader, axisYHeader) {
                axisXHeader += tableOperationsColumnWidth
            }

            newPage.canvas.drawTableCellValue(getLabelOperationType(data), axisXHeader, axisYHeader) {
                axisXHeader += tableOperationsColumnWidth
            }

            newPage.canvas.drawTableCellValue(data.quantity.toString(), axisXHeader, axisYHeader) {
                axisXHeader += tableOperationsColumnWidth
            }

            axisYHeader += MARGIN_8

            newPage.canvas.drawLineInPosition(
                startPosition = Position(axisX = MARGIN_30.toFloat(), axisY = axisYHeader),
                endPosition = Position(axisX = (tableOperationsColumnWidth * 4) + MARGIN_30.toFloat(), axisY = axisYHeader),
                paint = Paints.dashedLinePaint
            )

            axisYHeader += MARGIN_16
        }

        return newPage
    }

    private fun getLabelOperationType(data: StorageOperationReportData) = when (data.operationType) {
        EnumOperationType.Input -> context.getString(R.string.storage_operations_report_label_operation_type_input)
        EnumOperationType.ScheduledInput -> context.getString(R.string.storage_operations_report_label_operation_type_scheduled_input)
        EnumOperationType.Sell -> context.getString(R.string.storage_operations_report_label_operation_type_sell)
        EnumOperationType.Output -> context.getString(R.string.storage_operations_report_label_operation_type_output)
    }

    private suspend fun drawTableOperationsHeader(
        axisX: Float,
        axisY: Float,
        canvas: Canvas,
        onNextColumn: () -> Float,
        onFinish: () -> Unit
    ) = withContext(IO) {
        var x = axisX

        val labels = mutableListOf(
            context.getString(R.string.storage_operations_report_table_column_label_date_prevision),
            context.getString(R.string.storage_operations_report_table_column_label_date_realization),
            context.getString(R.string.storage_operations_report_table_column_label_operation_type),
            context.getString(R.string.storage_operations_report_table_column_label_quantity),
        )

        labels.forEach { column ->
            canvas.drawText(column, x, axisY, Paints.defaultLabelPaint)
            x = onNextColumn()
        }

        canvas.drawLineInPosition(
            startPosition = Position(axisX = axisX, axisY = axisY + MARGIN_8),
            endPosition = Position(axisX = x, axisY = axisY + MARGIN_8),
            paint = Paints.dashedLinePaint
        )

        onFinish()
    }
}