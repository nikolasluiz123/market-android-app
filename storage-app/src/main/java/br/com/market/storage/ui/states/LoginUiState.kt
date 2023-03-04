package br.com.market.storage.ui.states

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val serverError: String = "",
    val showErrorDialog: Boolean = false,
    val showLoading: Boolean = false,
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onValidate: () -> Boolean = { false },
    val onToggleErrorDialog: (String) -> Unit = {  },
    val onToggleLoading: () -> Unit = { }
)