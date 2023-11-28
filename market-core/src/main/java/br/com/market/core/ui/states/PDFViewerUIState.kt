package br.com.market.core.ui.states

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType

data class PDFViewerUIState(
    val uri: Uri? = null,
    val path: String? = null,
    var scrollOffset: Offset = Offset(0f, 0f),
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val onShowDialog: IShowDialogCallback? = null,
    override val onHideDialog: () -> Unit = { },
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { },
    override var internalErrorMessage: String = "",
) : IDialogUIState