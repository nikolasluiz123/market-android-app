package br.com.market.market.compose.components.input

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.R
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.states.input.InputTextUIState
import br.com.market.market.compose.components.button.icons.IconButtonClear
import br.com.market.market.compose.components.input.viewmodel.InputTextViewModel
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar

@Composable
fun InputText(
    viewModel: InputTextViewModel,
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: (String?) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    InputText(
        state = state,
        onBackClick = onBackClick,
        onConfirmClick = onConfirmClick,
        onCancelClick = onCancelClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    state: InputTextUIState = InputTextUIState(),
    onBackClick: () -> Unit = { },
    onCancelClick: () -> Unit = { },
    onConfirmClick: (String?) -> Unit = { }
) {
    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(id = state.titleResId!!),
                showMenuWithLogout = false,
                actions = {
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
                    if (state.maxLength == null || it.length <= state.maxLength!!) {
                        state.onValueChange(it)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                ),
                keyboardOptions = state.keyboardOptions,
                visualTransformation = state.visualTransformation,
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

                Button(
                    modifier = Modifier.constrainAs(buttonConfirmRef) {
                        top.linkTo(parent.top)
                        start.linkTo(buttonCancelRef.end, margin = 8.dp)
                    },
                    onClick = {
                        onConfirmClick(getValue(state))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(text = stringResource(R.string.text_advanced_filter_screen_label_confirm))
                }

                OutlinedButton(
                    modifier = Modifier.constrainAs(buttonCancelRef) {
                        linkTo(top = parent.top, bottom = parent.bottom, bias = 0f)
                        start.linkTo(parent.start, margin = 8.dp)
                    },
                    onClick = onCancelClick,
                ) {
                    Text(
                        text = stringResource(R.string.text_advanced_filter_screen_label_cancel),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

private fun getValue(state: InputTextUIState): String? {
    val value = state.value?.trim()
    return if (value?.isEmpty() == true) null else value
}

@Preview
@Composable
fun InputTextWithValuePreview() {
    MarketTheme {
        Surface {
            InputText(
                state = InputTextUIState(
                    titleResId = R.string.label_adicionar,
                    value = "Teste",
                    maxLength = 255
                )
            )
        }
    }
}

@Preview
@Composable
fun InputTextPreview() {
    MarketTheme {
        Surface {
            InputText(
                state = InputTextUIState(
                    titleResId = R.string.label_adicionar,
                    value = "",
                    maxLength = 255
                )
            )
        }
    }
}

@Preview
@Composable
fun InputTextWithLargeTextPreview() {
    MarketTheme {
        Surface {
            InputText(
                state = InputTextUIState(
                    titleResId = R.string.label_adicionar,
                    value = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
                    maxLength = null
                )
            )
        }
    }
}