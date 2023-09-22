package br.com.market.core.ui.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.R
import br.com.market.core.theme.colorSecondary
import br.com.market.core.ui.components.SimpleMarketTopAppBar
import br.com.market.core.ui.components.buttons.IconButtonClear
import br.com.market.core.ui.components.buttons.IconButtonVisibility
import br.com.market.core.ui.states.input.InputPasswordUIState
import br.com.market.core.ui.viewmodel.input.InputPasswordViewModel

@Composable
fun InputPassword(
    viewModel: InputPasswordViewModel,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (String?) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    InputPassword(
        state = state,
        onBackClick = onBackClick,
        onConfirmClick = onConfirmClick,
        onCancelClick = onCancelClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputPassword(
    state: InputPasswordUIState = InputPasswordUIState(),
    onBackClick: () -> Unit = { },
    onCancelClick: () -> Unit = { },
    onConfirmClick: (String?) -> Unit = { }
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(id = state.titleResId!!),
                showMenuWithLogout = false,
                actions = {
                    IconButtonVisibility(passwordVisible) {
                        passwordVisible = !passwordVisible
                    }

                    IconButtonClear {
                        state.onValueChange(null)
                    }
                },
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        ConstraintLayout(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val (textField, bottomBar) = createRefs()
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            TextField(
                modifier = Modifier
                    .constrainAs(textField) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        linkTo(top = parent.top, bottom = bottomBar.top, bias = 0F, bottomMargin = 8.dp)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .focusRequester(focusRequester),
                value = state.value ?: "",
                onValueChange = {
                    if (state.maxLength == null || it.length <= state.maxLength) {
                        state.onValueChange(it)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardOptions = state.keyboardOptions,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onConfirmClick(getValue(state))
                    }
                )
            )

            ConstraintLayout(
                modifier = Modifier.constrainAs(bottomBar) {
                    linkTo(start = parent.start, end = parent.end, bias = 0f)
                    bottom.linkTo(parent.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints
                }
            ) {
                val (buttonConfirmRef, buttonCancelRef, charCountRef) = createRefs()

                if (state.maxLength != null) {
                    Text(
                        modifier = Modifier.constrainAs(charCountRef) {
                            linkTo(top = parent.top, bottom = parent.bottom, bias = 0f)
                            top.linkTo(parent.top, margin = 8.dp)
                            end.linkTo(parent.end, margin = 8.dp)
                        },
                        text = "${state.value?.length ?: 0} / ${state.maxLength}"
                    )
                }

                OutlinedButton(
                    modifier = Modifier.constrainAs(buttonConfirmRef) {
                        top.linkTo(parent.top)
                        start.linkTo(buttonCancelRef.end, margin = 8.dp)
                    },
                    onClick = {
                        onConfirmClick(getValue(state))
                    },
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = colorSecondary),
                    border = null
                ) {
                    Text(stringResource(R.string.text_advanced_filter_screen_label_confirm), color = Color.White)
                }

                OutlinedButton(
                    modifier = Modifier.constrainAs(buttonCancelRef) {
                        linkTo(top = parent.top, bottom = parent.bottom, bias = 0f)
                        start.linkTo(parent.start, margin = 8.dp)
                    },
                    onClick = onCancelClick,
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, colorSecondary)
                ) {
                    Text(stringResource(R.string.text_advanced_filter_screen_label_cancel), color = colorSecondary)
                }
            }
        }
    }
}

private fun getValue(state: InputPasswordUIState): String? {
    val value = state.value?.trim()
    return if (value?.isEmpty() == true) null else value
}