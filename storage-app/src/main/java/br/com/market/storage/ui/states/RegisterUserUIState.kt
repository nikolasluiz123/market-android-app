package br.com.market.storage.ui.states

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