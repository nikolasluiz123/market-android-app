package br.com.market.storage.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.storage.R
import br.com.market.storage.ui.components.DialogMessage
import br.com.market.storage.ui.components.OutlinedTextFieldPasswordValidation
import br.com.market.storage.ui.components.OutlinedTextFieldValidation
import br.com.market.storage.ui.components.StorageAppLinearProgressIndicator
import br.com.market.storage.ui.domains.UserDomain
import br.com.market.storage.ui.states.LoginUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.theme.colorPrimary
import br.com.market.storage.ui.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

/**
 * Tela de login stateless.
 *
 * @param viewModel ViewModel da tela de login.
 * @param onAuthenticateSuccess Listener executado ao logar com sucesso.
 * @param onRegisterUserClick Listener executado ao clicar no botão de cadastrar-se.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onAuthenticateSuccess: () -> Unit = { },
    onRegisterUserClick: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LoginScreen(
        state = state,
        onAuthenticate = {
            coroutineScope.launch {
                state.onToggleLoading()
                val response = viewModel.authenticate(it)

                if (response.success) {
                    onAuthenticateSuccess()
                } else {
                    state.onToggleMessageDialog(response.error ?: "")
                }
                state.onToggleLoading()
            }
        },
        onRegisterUserClick = onRegisterUserClick
    )
}

/**
 * Tela de login statefull.
 *
 * @param state Estado da tela de login.
 * @param onAuthenticate Listener executado ao clicar em login.
 * @param onRegisterUserClick Listener executado ao clicar em cadastrar-se
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun LoginScreen(
    state: LoginUiState = LoginUiState(),
    onAuthenticate: (UserDomain) -> Unit = {},
    onRegisterUserClick: () -> Unit = { }
) {
    ConstraintLayout(
        Modifier.fillMaxWidth()
    ) {
        val (loadingRef) = createRefs()

        StorageAppLinearProgressIndicator(
            state.showLoading,
            Modifier
                .constrainAs(loadingRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (emailRef, passwordRef, loginButtonRef, registerButtonRef) = createRefs()

            DialogMessage(
                title =  stringResource(R.string.error_dialog_title),
                show = state.showDialogMessage,
                onDismissRequest = { state.onToggleMessageDialog("") },
                message = state.serverMessage
            )

            OutlinedTextFieldValidation(
                modifier = Modifier.constrainAs(emailRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                },
                value = state.email,
                onValueChange = state.onEmailChange,
                error = state.emailErrorMessage,
                label = { Text(text = stringResource(R.string.login_screen_label_email)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextFieldPasswordValidation(
                modifier = Modifier.constrainAs(passwordRef) {
                    start.linkTo(emailRef.start)
                    end.linkTo(emailRef.end)
                    top.linkTo(emailRef.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                },
                value = state.password,
                onValueChange = state.onPasswordChange,
                error = state.passwordErrorMessage,
                label = { Text(text = stringResource(R.string.login_screen_label_password)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            createHorizontalChain(registerButtonRef, loginButtonRef)

            OutlinedButton(
                modifier = Modifier
                    .constrainAs(registerButtonRef) {
                        start.linkTo(parent.start)
                        top.linkTo(loginButtonRef.top)

                        horizontalChainWeight = 0.45F

                        width = Dimension.fillToConstraints
                    }
                    .padding(end = 8.dp),
                onClick = onRegisterUserClick,
                border = BorderStroke(1.0.dp, colorPrimary),
                enabled = !state.showLoading
            ) {
                Text(text = stringResource(R.string.login_screen_label_button_register_user))
            }

            Button(
                modifier = Modifier.constrainAs(loginButtonRef) {
                    end.linkTo(parent.end)
                    top.linkTo(passwordRef.bottom, margin = 8.dp)

                    horizontalChainWeight = 0.45F

                    width = Dimension.fillToConstraints
                },
                onClick = {
                    if (state.onValidate()) {
                        onAuthenticate(UserDomain(email = state.email, password = state.password))
                    }
                },
                enabled = !state.showLoading
            ) {
                Text(text = stringResource(R.string.login_screen_label_button_login))
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    StorageTheme {
        Surface {
            LoginScreen(state = LoginUiState())
        }
    }
}