package br.com.market.storage.ui.states.report

import br.com.market.core.enums.EnumDialogType
import br.com.market.core.interfaces.IShowDialog
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.market.pdf.generator.common.ReportFile

data class ReportSearchUIState(
    val reports: List<ReportFile> = mutableListOf(),
    val reportClicked: ReportFile? = null,
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val onShowDialog: IShowDialog? = null,
    override val onHideDialog: () -> Unit = { },
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { }
): IDialogUIState
