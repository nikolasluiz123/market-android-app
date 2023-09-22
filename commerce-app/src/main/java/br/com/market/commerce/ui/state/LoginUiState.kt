package br.com.market.commerce.ui.state

import br.com.market.core.ui.states.Field
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IServerErrorHandlerUIState
import br.com.market.core.ui.states.IValidationUIState

data class LoginUiState(
    val email: Field = Field(),
    val password: Field = Field(),
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false }
) : IValidationUIState, ILoadingUIState, IServerErrorHandlerUIState