package br.com.market.storage.ui.states.product

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.interfaces.IShowDialog
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.ProductDomain
import br.com.market.domain.ProductImageDomain
import br.com.market.enums.EnumUnit

data class ProductUIState(
    var categoryId: String? = null,
    var brandDomain: BrandDomain? = null,
    var productDomain: ProductDomain? = null,

    val productName: String = "",
    val onProductNameChange: (String) -> Unit = { },
    val productNameErrorMessage: String = "",

    val productPrice: String = "",
    val onProductPriceChange: (String) -> Unit = { },
    val productPriceErrorMessage: String = "",

    val productQuantity: String = "",
    val onProductQuantityChange: (String) -> Unit = { },
    val productQuantityErrorMessage: String = "",

    var productQuantityUnit: EnumUnit? = null,
    val productQuantityUnitErrorMessage: String = "",

    var images: SnapshotStateList<ProductImageDomain> = mutableStateListOf(),

    override val onValidate: () -> Boolean = { false },
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val onHideDialog: () -> Unit = { },
    override val onShowDialog: IShowDialog? = null,
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { }
): IValidationUIState, IDialogUIState
