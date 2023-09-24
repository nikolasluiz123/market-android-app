package br.com.market.market.pdf.generator.reports

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument.Page
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.format
import br.com.market.market.pdf.generator.R
import br.com.market.market.pdf.generator.enums.EnumPageSize
import br.com.market.market.pdf.generator.extensions.drawLineInPosition
import br.com.market.market.pdf.generator.extensions.drawTextInPosition
import br.com.market.market.pdf.generator.utils.Margins
import br.com.market.market.pdf.generator.utils.Paints
import br.com.market.market.pdf.generator.utils.Position
import br.com.market.report.data.ReportCommonData
import java.time.LocalDateTime

abstract class BaseReportGenerator(val context: Context) {

    abstract suspend fun generateReport()

    open suspend fun drawReportHeader(page: Page, commonData: ReportCommonData): Position {
        var bitmap = BitmapFactory.decodeByteArray(commonData.logo, 0, commonData.logo.size)
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true)

        val reportTitlePosition = Position(
            axisX = Margins.MARGIN_30.toFloat(),
            axisY = Margins.MARGIN_30.toFloat()
        )
        page.canvas.drawTextInPosition(
            text = context.getString(R.string.storage_operations_report_title),
            position = reportTitlePosition,
            paint = Paints.titlePaint
        )

        val startLineTitlePosition = Position(
            axisX = reportTitlePosition.axisX,
            axisY = reportTitlePosition.axisY + Margins.MARGIN_8
        )
        val endLineTitlePosition = Position(
            axisX = (page.info.pageWidth - Margins.MARGIN_30 - bitmap.width).toFloat(),
            axisY = reportTitlePosition.axisY + Margins.MARGIN_8
        )
        page.canvas.drawLineInPosition(
            startPosition = startLineTitlePosition,
            endPosition = endLineTitlePosition,
            paint = Paints.titleLinePaint
        )

        page.canvas.drawBitmap(
            bitmap,
            (page.info.pageWidth - bitmap.width).toFloat(),
            0f,
            Paints.defaultLabelPaint
        )

        return Position(
            axisX = reportTitlePosition.axisX,
            axisY = bitmap.height.toFloat()
        )
    }

    open suspend fun drawReportFooter(page: Page, commonData: ReportCommonData): Position {
        val actualDate = LocalDateTime.now().format(EnumDateTimePatterns.DATE_TIME)

        val labelDate = context.getString(R.string.storage_operations_report_label_date)
        val labelDatePosition = Position(
            axisX = Margins.MARGIN_30.toFloat(),
            axisY = EnumPageSize.A4.height.toFloat() - Margins.MARGIN_30
        )
        page.canvas.drawTextInPosition(
            text = labelDate,
            position = labelDatePosition,
            Paints.defaultLabelPaint
        )

        val datePosition = Position(
            axisX = Paints.defaultLabelPaint.measureText(labelDate) + Margins.MARGIN_8 + Margins.MARGIN_30,
            axisY = labelDatePosition.axisY
        )
        page.canvas.drawTextInPosition(
            text = actualDate,
            position = datePosition,
            paint = Paints.defaultValuePaint
        )

        val labelMarket = context.getString(R.string.storage_operations_report_label_market)
        val labelMarketPosition = Position(
            axisX = labelDatePosition.axisX,
            axisY = labelDatePosition.axisY - Margins.MARGIN_16
        )
        page.canvas.drawTextInPosition(
            text = labelMarket,
            position = labelMarketPosition,
            Paints.defaultLabelPaint
        )

        val marketPosition = Position(
            axisX = Paints.defaultLabelPaint.measureText(labelMarket) + Margins.MARGIN_8 + Margins.MARGIN_30,
            axisY = labelMarketPosition.axisY
        )
        page.canvas.drawTextInPosition(
            text = commonData.marketName,
            position = marketPosition,
            paint = Paints.defaultValuePaint
        )

        val companyLabelPosition = Position(
            axisX = labelMarketPosition.axisX,
            axisY = labelMarketPosition.axisY - Margins.MARGIN_16
        )

        val labelCompany = context.getString(R.string.storage_operations_report_label_company)
        page.canvas.drawTextInPosition(
            text = labelCompany,
            position = companyLabelPosition,
            paint = Paints.defaultLabelPaint
        )

        val companyPosition = Position(
            axisX = Paints.defaultLabelPaint.measureText(labelCompany) + Margins.MARGIN_8 + Margins.MARGIN_30,
            axisY = companyLabelPosition.axisY
        )
        page.canvas.drawTextInPosition(
            text = commonData.companyName,
            position = companyPosition,
            paint = Paints.defaultValuePaint
        )

        val startLineFooterPosition = Position(
            axisX = companyLabelPosition.axisX,
            axisY = companyLabelPosition.axisY - Margins.MARGIN_30
        )
        val endLineFooterPosition = Position(
            axisX = (page.info.pageWidth - Margins.MARGIN_30).toFloat(),
            axisY = companyLabelPosition.axisY - Margins.MARGIN_30
        )
        page.canvas.drawLineInPosition(
            startPosition = startLineFooterPosition,
            endPosition = endLineFooterPosition,
            paint = Paints.titleLinePaint
        )

        return Position(
            axisX = labelDatePosition.axisX,
            axisY = labelDatePosition.axisY
        )
    }
}