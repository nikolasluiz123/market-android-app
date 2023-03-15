package br.com.market.storage.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.*
import br.com.market.domain.UserDomain
import br.com.market.storage.R
import br.com.market.storage.ui.states.RegisterUserUIState
import br.com.market.storage.ui.viewmodels.RegisterUserViewModel
import kotlinx.coroutines.launch

/**
 * Tela de cadastro do usuário stateless.
 *
 * @param viewModel ViewModel da tela.
 * @param onNavigationClick Listener executado a oclicar no ícone de voltar.
 * @param onRegisterSuccess Listener executado ao registrar-se com sucesso.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun RegisterUserScreen(
    viewModel: RegisterUserViewModel,
    onNavigationClick: () -> Unit = { },
    onRegisterSuccess: () -> Unit = { }
) {
    val state by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    RegisterUserScreen(
        state = state,
        onButtonSaveClick = {
            coroutineScope.launch {
                state.onToggleLoading()
                val response = viewModel.registerUser(it)

                if (response.success) {
                    onRegisterSuccess()
                } else {
                    state.onToggleMessageDialog(response.error ?: "")
                }

                state.onToggleLoading()
            }
        },
        onNavigationClick = onNavigationClick
    )
}

/**
 * Tela de cadastro do usuário statefull
 *
 * @param state Objeto de estado da tela.
 * @param onButtonSaveClick Listener executado quando clicar em salvar.
 * @param onNavigationClick Listener executado quando clicar no botão de voltar.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserScreen(
    state: RegisterUserUIState = RegisterUserUIState(),
    onButtonSaveClick: (UserDomain) -> Unit = { },
    onNavigationClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            MarketTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.register_user_screen_top_app_bar_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                onNavigationIconClick = onNavigationClick,
                showMenuWithLogout = false
            )
        }
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (nameRef, emailRef, passwordRef, registerButtonRef, loadingRef) = createRefs()

            MarketLinearProgressIndicator(
                state.showLoading,
                Modifier
                    .constrainAs(loadingRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )

            DialogMessage(
                title = stringResource(R.string.error_dialog_title),
                show = state.showDialogMessage,
                onDismissRequest = { state.onToggleMessageDialog("") },
                message = state.serverMessage
            )

            OutlinedTextFieldValidation(
                modifier = Modifier.constrainAs(nameRef) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(loadingRef.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                },
                value = state.name,
                onValueChange = state.onNameChange,
                error = state.nameErrorMessage,
                label = { Text(text = stringResource(R.string.register_user_screen_label_name)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                )
            )

            OutlinedTextFieldValidation(
                modifier = Modifier.constrainAs(emailRef) {
                    start.linkTo(nameRef.start)
                    end.linkTo(nameRef.end)
                    top.linkTo(nameRef.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                },
                value = state.email,
                onValueChange = state.onEmailChange,
                error = state.emailErrorMessage,
                label = { Text(text = stringResource(R.string.register_user_screen_label_email)) },
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
                label = { Text(text = stringResource(R.string.register_user_screen_label_password)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                modifier = Modifier.constrainAs(registerButtonRef) {
                    start.linkTo(passwordRef.start)
                    end.linkTo(passwordRef.end)
                    top.linkTo(passwordRef.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                },
                onClick = {
                    if (state.onValidate()) {
                        onButtonSaveClick(
                            UserDomain(
                                name = state.name,
                                email = state.email,
                                password = state.password
                            )
                        )
                    }
                }
            ) {
                Text(text = stringResource(R.string.register_user_screen_label_button_register))
            }
        }
    }

}

@Preview
@Composable
fun RegisterUserPreview() {
    MarketTheme {
        Surface {
            RegisterUserScreen()
        }
    }
}