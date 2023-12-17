package br.com.market.storage.ui.states

import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.domain.ProductImageDomain

data class ImageViewerUIState(
    val productName: String? = null,
    val productImageDomain: ProductImageDomain? = null,
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val onShowDialog: IShowDialogCallback? = null,
    override val onHideDialog: () -> Unit = { },
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { },
    override var internalErrorMessage: String = "",
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { }
): ILoadingUIState, IDialogUIState
