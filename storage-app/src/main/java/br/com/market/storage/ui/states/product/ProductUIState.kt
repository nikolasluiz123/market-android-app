package br.com.market.storage.ui.states.product

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.ui.states.Field
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductDomain
import br.com.market.domain.ProductImageDomain

data class ProductUIState(
    var categoryId: String? = null,
    var categoryBrandId: String? = null,
    var brandDomain: BrandDomain? = null,
    var productDomain: ProductDomain? = null,
    val name: Field = Field(),
    val price: Field = Field(),
    val quantity: Field = Field(),
    val quantityUnit: Field = Field(),
    var images: SnapshotStateList<ProductImageDomain> = mutableStateListOf(),
    override val onValidate: () -> Boolean = { false },
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val onHideDialog: () -> Unit = { },
    override val onShowDialog: IShowDialogCallback? = null,
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { },
    override var internalErrorMessage: String = "",
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { }
): IValidationUIState, IDialogUIState, ILoadingUIState
