package br.com.market.commerce.ui.state

import androidx.paging.PagingData
import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.domain.ProductImageReadDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProductsUIState(
    var products: Flow<PagingData<ProductImageReadDomain>> = emptyFlow(),
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val onHideDialog: () -> Unit = { },
    override val onShowDialog: IShowDialogCallback? = null,
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { },
    override var internalErrorMessage: String = ""
) : ILoadingUIState, IDialogUIState