package br.com.market.commerce.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.commerce.R
import br.com.market.commerce.ui.state.LoginUiState
import br.com.market.commerce.ui.viewmodel.LoginViewModel
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.theme.MarketTheme
import br.com.market.domain.UserDomain
import br.com.market.market.compose.components.OutlinedTextFieldPasswordValidation
import br.com.market.market.compose.components.OutlinedTextFieldValidation
import br.com.market.market.compose.components.dialog.MarketDialog
import br.com.market.market.compose.components.loading.MarketLinearProgressIndicator
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit,
    onAuthenticateSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LoginScreen(
        state = state,
        onRegisterClick = onRegisterClick,
        onAuthenticate = {
            state.onToggleLoading()

            viewModel.authenticate(it) { response ->
                if (response.success) {
                    onAuthenticateSuccess()
                } else {
                    state.onShowDialog?.onShow(
                        type = EnumDialogType.ERROR,
                        message = response.error ?: "",
                        onConfirm = {},
                        onCancel = {}
                    )
                }
                state.onToggleLoading()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginUiState = LoginUiState(),
    onRegisterClick: () -> Unit = { },
    onAuthenticate: (UserDomain) -> Unit = {}
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(R.string.login_screen_label_title),
                showNavigationIcon = false,
                showMenuWithLogout = false
            )
        }
    ) { padding ->
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (loadingRef, containerRef) = createRefs()

            MarketLinearProgressIndicator(
                state.showLoading,
                Modifier
                    .constrainAs(loadingRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )

            Column(
                modifier = Modifier
                    .constrainAs(containerRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(loadingRef.bottom, margin = 16.dp)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                ConstraintLayout(Modifier.fillMaxWidth()) {
                    val (emailRef, passwordRef, loginButtonRef, registerButtonRef) = createRefs()

                    MarketDialog(
                        type = state.dialogType,
                        show = state.showDialog,
                        onDismissRequest = { state.onHideDialog() },
                        message = state.dialogMessage
                    )

                    OutlinedTextFieldValidation(
                        modifier = Modifier.constrainAs(emailRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)

                            width = Dimension.fillToConstraints
                        },
                        field = state.email,
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
                        field = state.password,
                        label = { Text(text = stringResource(R.string.login_screen_label_password)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    createHorizontalChain(registerButtonRef, loginButtonRef)

                    Button(
                        modifier = Modifier
                            .constrainAs(loginButtonRef) {
                                start.linkTo(parent.start)
                                top.linkTo(passwordRef.bottom, margin = 8.dp)

                                horizontalChainWeight = 0.45F

                                width = Dimension.fillToConstraints
                            }
                            .padding(start = 8.dp),
                        onClick = {
                            if (state.onValidate()) {
                                onAuthenticate(
                                    UserDomain(
                                        email = state.email.value,
                                        password = state.password.value
                                    )
                                )
                            }
                        },
                        enabled = !state.showLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(text = stringResource(R.string.login_screen_label_button_login))
                    }

                    OutlinedButton(
                        modifier = Modifier.constrainAs(registerButtonRef) {
                            end.linkTo(parent.end)
                            top.linkTo(passwordRef.bottom, margin = 8.dp)

                            horizontalChainWeight = 0.45F

                            width = Dimension.fillToConstraints
                        },
                        onClick = onRegisterClick,
                        enabled = !state.showLoading,
                    ) {
                        Text(
                            text = stringResource(R.string.login_screen_label_button_register),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

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