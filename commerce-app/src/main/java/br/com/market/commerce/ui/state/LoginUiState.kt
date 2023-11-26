package br.com.market.commerce.ui.state

import br.com.market.core.enums.EnumDialogType
import br.com.market.core.interfaces.IShowDialog
import br.com.market.core.ui.states.Field
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IValidationUIState

data class LoginUiState(
    val email: Field = Field(),
    val password: Field = Field(),
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false },
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val onHideDialog: () -> Unit = { },
    override val onShowDialog: IShowDialog? = null,
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { }
) : IValidationUIState, ILoadingUIState, IDialogUIState