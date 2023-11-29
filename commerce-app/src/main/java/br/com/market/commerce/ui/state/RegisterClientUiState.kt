package br.com.market.commerce.ui.state

import br.com.market.core.callbacks.IShowDialogCallback
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.ui.states.Field
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IValidationUIState

data class RegisterClientUiState(
    val name: Field = Field(),
    val cpf: Field = Field(),
    val email: Field = Field(),
    val password: Field = Field(),
    val cep: Field = Field(),
    val state: Field = Field(),
    val city: Field = Field(),
    val publicPlace: Field = Field(),
    val number: Field = Field(),
    val complement: Field = Field(),
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val dialogMessage: String = "",
    override val showDialog: Boolean = false,
    override val dialogType: EnumDialogType = EnumDialogType.ERROR,
    override val onHideDialog: () -> Unit = {},
    override val onValidate: () -> Boolean = { false },
    override val onShowDialog: IShowDialogCallback? = null,
    override val onConfirm: () -> Unit = { },
    override val onCancel: () -> Unit = { },
    override var internalErrorMessage: String = ""
) : IValidationUIState, ILoadingUIState, IDialogUIState