package br.com.market.storage.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.market.storage.ui.components.OutlinedTextFieldValidation
import br.com.market.storage.ui.states.LoginUiState
import br.com.market.storage.ui.theme.StorageTheme
import br.com.market.storage.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    LoginScreen(
        state = state,
        onLoginClick = onLoginClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginUiState = LoginUiState(),
    onLoginClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {

        OutlinedTextFieldValidation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.email,
            onValueChange = state.onEmailChange,
            error = state.emailErrorMessage,
            label = { Text(text = "E-mail") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextFieldValidation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.password,
            onValueChange = state.onPasswordChange,
            error = state.passwordErrorMessage,
            label = { Text(text = "Senha") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (state.onValidate()) {
                    onLoginClick()
                }
            }
        ) {
            Text(text = "Login")
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