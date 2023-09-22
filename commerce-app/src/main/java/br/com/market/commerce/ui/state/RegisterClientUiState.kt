package br.com.market.commerce.ui.state

import br.com.market.core.ui.states.Field
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IServerErrorHandlerUIState
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
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState, ILoadingUIState, IServerErrorHandlerUIState