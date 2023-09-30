package br.com.market.core.ui.states

import android.net.Uri
import androidx.compose.ui.geometry.Offset

data class PDFViewerUIState(
    val uri: Uri? = null,
    val path: String? = null,
    var scrollOffset: Offset = Offset(0f, 0f),
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = {},
) : IServerErrorHandlerUIState