package br.com.market.storage.ui.states

import br.com.market.core.enums.EnumDialogType
import br.com.market.core.interfaces.IShowDialog
import br.com.market.core.ui.states.IDialogUIState
import br.com.market.core.ui.states.ILoadingUIState
import br.com.market.core.ui.states.IValidationUIState

/**
 * Classe de estado da tela de login
 *
 * @property email E-mail digitado
 * @property password Senha digitada
 * @property emailErrorMessage Mensagem de erro do e-mail
 * @property passwordErrorMessage Mensagem de erro da senha
 * @property onEmailChange Listener executado quando o e-mail é alterado no campo de texto
 * @property onPasswordChange Listener executado quando a senha é altarada no campo de texto
 *
 * @author Nikolas Luiz Schmitt
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
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