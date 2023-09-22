package br.com.market.storage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.MarketLinearProgressIndicator
import br.com.market.core.ui.components.OutlinedTextFieldPasswordValidation
import br.com.market.core.ui.components.OutlinedTextFieldValidation
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.dialog.DialogMessage
import br.com.market.domain.UserDomain
import br.com.market.storage.ui.states.LoginUiState
import br.com.market.storage.ui.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

/**
 * Tela de login stateless.
 *
 * @param viewModel ViewModel da tela de login.
 * @param onAuthenticateSuccess Listener executado ao logar com sucesso.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onAuthenticateSuccess: () -> Unit = { },
    onAboutClick: () -> Unit
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
        onAboutClick = onAboutClick
    )
}

/**
 * Tela de login statefull.
 *
 * @param state Estado da tela de login.
 * @param onAuthenticate Listener executado ao clicar em login.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginUiState = LoginUiState(),
    onAuthenticate: (UserDomain) -> Unit = {},
    onAboutClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Bem-Vindo",
                menuItems = {
                    DropdownMenuItem(text = { Text("Sobre") }, onClick = onAboutClick)
                }
            )
        }
    ) { padding ->
        ConstraintLayout(
            Modifier.fillMaxWidth()
        ) {
            val (loadingRef) = createRefs()

            MarketLinearProgressIndicator(
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
                .padding(padding)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (emailRef, passwordRef, loginButtonRef, registerButtonRef) = createRefs()

                DialogMessage(
                    title =  stringResource(br.com.market.core.R.string.error_dialog_title),
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
                    label = { Text(text = stringResource(br.com.market.core.R.string.login_screen_label_email)) },
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
                    label = { Text(text = stringResource(br.com.market.core.R.string.login_screen_label_password)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )

                createHorizontalChain(registerButtonRef, loginButtonRef)

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
                    Text(text = stringResource(br.com.market.core.R.string.login_screen_label_button_login))
                }

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    MarketTheme {
        Surface {
            LoginScreen(state = LoginUiState())
        }
    }
}