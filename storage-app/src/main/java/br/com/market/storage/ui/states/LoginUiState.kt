package br.com.market.storage.ui.states

import br.com.market.core.ui.states.BaseUiState

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
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false }
) : BaseUiState()