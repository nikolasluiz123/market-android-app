package br.com.market.core.ui.states

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.interfaces.IShowDialog

data class PDFViewerUIState(
    val uri: Uri? = null,
    val path: String? = null,
    var scrollOffset: Offset = Offset(0f, 0f),
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val onShowDialog: IShowDialog? = null,
    override val onHideDialog: () -> Unit = { },
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { },
) : IDialogUIState