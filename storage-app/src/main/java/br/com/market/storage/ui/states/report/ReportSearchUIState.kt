package br.com.market.storage.ui.states.report

import br.com.market.market.pdf.generator.common.ReportFile

data class ReportSearchUIState(
    val reports: List<ReportFile> = mutableListOf(),
    val reportClicked: ReportFile? = null
)
