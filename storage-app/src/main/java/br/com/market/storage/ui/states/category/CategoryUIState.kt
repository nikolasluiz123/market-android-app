package br.com.market.storage.ui.states.category

import androidx.paging.PagingData
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.interfaces.IShowDialog
import br.com.market.core.ui.states.Field
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IValidationUIState
import br.com.market.domain.BrandDomain
import br.com.market.domain.CategoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CategoryUIState(
    var categoryDomain: CategoryDomain? = null,
    val nameField: Field = Field(),
    var brands: Flow<PagingData<BrandDomain>> = emptyFlow(),
    override val onValidate: () -> Boolean = { false },
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val showLoading: Boolean = false,
    override val onHideDialog: () -> Unit = { },
    override val onToggleLoading: () -> Unit = { },
    override val onShowDialog: IShowDialog? = null,
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { }
) : IValidationUIState, IDialogUIState, ILoadingUIState