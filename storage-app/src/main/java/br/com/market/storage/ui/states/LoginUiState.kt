package br.com.market.storage.ui.states

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onValidate: () -> Boolean = { false }
)