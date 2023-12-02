package br.com.market.storage.ui.states.brand

import androidx.paging.PagingData
import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.ui.states.Field
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.CategoryDomain
import br.com.market.domain.ProductImageReadDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BrandUIState(
    var categoryDomain: CategoryDomain? = null,
    var brandDomain: BrandDomain? = null,
    var active: Boolean = true,
    val name: Field = Field(),
    var products: Flow<PagingData<ProductImageReadDomain>> = emptyFlow(),
    override val onValidate: () -> Boolean = { false },
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
) : IValidationUIState, IDialogUIState, ILoadingUIState