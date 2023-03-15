package br.com.market.storage.ui.states

import br.com.market.core.ui.states.BaseUiState

/**
 * Classe de estado da tela de cadastro do usuário
 *
 * @property name Nome do usuário
 * @property email E-mail do usuário
 * @property password Senha do usuário
 * @property nameErrorMessage Mensagem de erro do nome do usuário
 * @property emailErrorMessage Mensagem de erro do e-mail do usuário
 * @property passwordErrorMessage Mensagem de erro da senha do usuário
 * @property onNameChange Listener executado quando o nome do usuário é alterado no campo de texto
 * @property onEmailChange Listener executado quando o nome do usuário é alterado no campo de texto
 * @property onPasswordChange Listener executado quando a senha do usuário é alterada no campo de texto
 *
 * @author Nikolas Luiz Schmitt
 */
data class RegisterUserUIState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val nameErrorMessage: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val onNameChange: (String) -> Unit = { },
    val onEmailChange: (String) -> Unit = { },
    val onPasswordChange: (String) -> Unit = { },
    override val serverMessage: String = "",
    override val showDialogMessage: Boolean = false,
    override val onToggleMessageDialog: (String) -> Unit = { },
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
    override val onValidate: () -> Boolean = { false },
) : BaseUiState()